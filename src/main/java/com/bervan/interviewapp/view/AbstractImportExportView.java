package com.bervan.interviewapp.view;

import com.bervan.common.view.AbstractPageView;
import com.bervan.common.model.PersistableTableData;
import com.bervan.common.service.BaseService;

import java.util.List;
import java.util.UUID;


public abstract class AbstractImportExportView extends AbstractPageView {
    public static final String ROUTE_NAME = "interview-app/import-export-data";

    public AbstractImportExportView(List<BaseService<UUID, ? extends PersistableTableData<?>>> dataServices) {
        super();

        showErrorNotification("Not implemented!");
    }
}
