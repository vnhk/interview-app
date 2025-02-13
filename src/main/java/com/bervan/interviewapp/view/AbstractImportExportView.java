package com.bervan.interviewapp.view;

import com.bervan.common.AbstractPageView;
import com.bervan.common.onevalue.OneValue;
import com.bervan.common.onevalue.OneValueService;
import com.bervan.core.model.BervanLogger;
import com.bervan.ieentities.BaseExcelExport;
import com.bervan.ieentities.BaseExcelImport;
import com.bervan.ieentities.ExcelIEEntity;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.pocketitem.CodingTask;
import com.bervan.interviewapp.pocketitem.CodingTaskService;
import com.bervan.interviewapp.questionconfig.QuestionConfig;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public abstract class AbstractImportExportView extends AbstractPageView {
    public static final String ROUTE_NAME = "interview-app/import-export-data";
    private final CodingTaskService codingTaskService;
    private final OneValueService oneValueService;
    private final InterviewQuestionService interviewQuestionService;
    private InterviewAppPageLayout pageLayout;
    private final BervanLogger logger;
    @Value("${file.service.storage.folder}")
    private String pathToFileStorage;
    @Value("${global-tmp-dir.file-storage-relative-path}")
    private String globalTmpDir;

    public AbstractImportExportView(CodingTaskService codingTaskService,
                                    OneValueService oneValueService,
                                    InterviewQuestionService interviewQuestionService,
                                    BervanLogger logger) {
        this.logger = logger;
        pageLayout = new InterviewAppPageLayout(ROUTE_NAME);
        add(pageLayout);
        this.codingTaskService = codingTaskService;
        this.oneValueService = oneValueService;
        this.interviewQuestionService = interviewQuestionService;

        Button prepareExportButton = new Button("Prepare data for export");
        prepareExportButton.addClassName("option-button");
        add(prepareExportButton);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream();
            try {
                importData(inputStream, fileName);
                Notification.show("File uploaded successfully: " + fileName);
            } catch (Exception e) {
                logger.error("Failed to upload file: " + fileName, e);
                Notification.show("Failed to upload file: " + fileName);
            }
        });

        upload.addFailedListener(event ->
                Notification.show("Upload failed"));

        prepareExportButton.addClickListener(buttonClickEvent -> {
            StreamResource resource = prepareDownloadResource();
            Anchor downloadLink = new Anchor(resource, "Export");
            downloadLink.getElement().setAttribute("download", true);

            add(downloadLink);
            remove(prepareExportButton);
            remove(upload);
            add(upload);
        });

        add(upload);
    }

    private void importData(InputStream inputStream, String fileName) throws IOException {
        File uploadFolder = new File(pathToFileStorage + globalTmpDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        File file = new File(pathToFileStorage + globalTmpDir + File.separator + fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] buffer = new byte[10240];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        List<Class<?>> subclasses = new ArrayList<>();
        subclasses.add(CodingTask.class);
        subclasses.add(OneValue.class);
        subclasses.add(QuestionConfig.class);
        subclasses.add(Question.class);

        logger.debug("Class that will be imported: " + subclasses);
        BaseExcelImport baseExcelImport = new BaseExcelImport(subclasses, logger);
        List<? extends ExcelIEEntity<UUID>> objects = (List<? extends ExcelIEEntity<UUID>>) baseExcelImport.importExcel(baseExcelImport.load(file));
        logger.debug("Extracted " + objects.size() + " entities from excel.");
        codingTaskService.saveIfValid(objects);
        oneValueService.saveIfValid(objects);
        interviewQuestionService.saveIfValid(objects);
    }

    public StreamResource prepareDownloadResource() {
        try {
            BaseExcelExport baseExcelExport = new BaseExcelExport();
            Workbook workbook = baseExcelExport.exportExcel(getDataToExport(), null);
            File saved = baseExcelExport.save(workbook, pathToFileStorage + globalTmpDir, "export" + LocalDateTime.now() + ".xlsx");
            String filename = saved.getName();

            return new StreamResource(filename, () -> {
                try {
                    return new FileInputStream(saved);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            logger.error("Could not prepare export data.", e);
            showErrorNotification("Could not prepare export data.");
        }

        return null;
    }

    private List<ExcelIEEntity<?>> getDataToExport() {
        List<ExcelIEEntity<?>> result = new ArrayList<>();
        result.addAll(codingTaskService.load(Pageable.ofSize(1000000)));
        result.addAll(interviewQuestionService.load(Pageable.ofSize(1000000)));
        result.addAll(oneValueService.load());
        return result;
    }
}
