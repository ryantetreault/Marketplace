module com.cboard.marketplace.marketplace_common {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires jakarta.validation;
    exports com.cboard.marketplace.marketplace_common;
    exports com.cboard.marketplace.marketplace_common.dto;

    opens com.cboard.marketplace.marketplace_common to com.google.gson;
    opens com.cboard.marketplace.marketplace_common.dto to com.google.gson;
}