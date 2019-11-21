package de.aqua.web.utils.enums;

/**
 * Created by bdumitru on 8/10/2017.
 */
public enum Pipelines {

    ManualCalibration("Manual calibration"),
    ManualImaging("Manual imaging"),
    ManualSingleDish("SD Manual processing"),
    ManualCombination(""),
    PipelineCalibration("PL calibration"),
    PipelineImaging("PL imaging"),
    PipelineSingleDish("SD PL processing"),
    PipelineCombination(""),
    PreparePackage(""),
    TransferToJao(""),
    IngestAtJao(""),
    PipelineCalAndImg("PL process");

    private final String value;

    Pipelines(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
