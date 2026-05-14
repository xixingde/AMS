package com.cmhk.ams.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cmhk.ams.exception.BusinessException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class ExcelImportListener<T> extends AnalysisEventListener<T> {

    private static final int BATCH_COUNT = 100;
    private final List<T> cachedDataList = new ArrayList<>();
    private final Consumer<List<T>> dataConsumer;
    @Getter
    private int successCount = 0;
    @Getter
    private int failCount = 0;
    @Getter
    private final List<String> failMessages = new ArrayList<>();

    public ExcelImportListener(Consumer<List<T>> dataConsumer) {
        this.dataConsumer = dataConsumer;
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            consumeData();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (!cachedDataList.isEmpty()) {
            consumeData();
        }
    }

    private void consumeData() {
        try {
            dataConsumer.accept(new ArrayList<>(cachedDataList));
            successCount += cachedDataList.size();
        } catch (BusinessException e) {
            failCount += cachedDataList.size();
            failMessages.add(e.getMessage());
            log.warn("Excel import batch failed: {}", e.getMessage());
        } catch (Exception e) {
            failCount += cachedDataList.size();
            failMessages.add(e.getMessage());
            log.error("Excel import batch error", e);
        }
        cachedDataList.clear();
    }
}
