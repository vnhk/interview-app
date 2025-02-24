package com.bervan.interviewapp.view;

import com.bervan.common.AbstractDataIEView;
import com.bervan.common.EmptyLayout;
import com.bervan.common.model.PersistableTableData;
import com.bervan.common.service.BaseService;
import com.bervan.core.model.BervanLogger;
import com.bervan.interviewapp.interviewquestions.InterviewQuestionService;
import com.bervan.interviewapp.interviewquestions.Question;
import com.bervan.interviewapp.codingtask.CodingTask;
import com.bervan.interviewapp.questionconfig.QuestionConfig;

import java.util.List;
import java.util.UUID;


public abstract class AbstractImportExportView extends AbstractDataIEView<UUID> {
    public static final String ROUTE_NAME = "interview-app/import-export-data";

    public AbstractImportExportView(List<BaseService<UUID, ? extends PersistableTableData<?>>> dataServices, BervanLogger logger) {
        super(dataServices, new EmptyLayout(), logger,
                List.of(Question.class,
                        QuestionConfig.class,
                        CodingTask.class));
    }
}
