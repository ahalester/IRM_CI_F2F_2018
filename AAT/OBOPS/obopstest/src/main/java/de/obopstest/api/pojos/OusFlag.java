package de.obopstest.api.pojos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "flagName",
        "flagValue",
        "updateAccountId",
        "updated"
})
public class OusFlag implements Serializable
{

    @JsonProperty("flagName")
    private String flagName;
    @JsonProperty("flagValue")
    private String flagValue;
    @JsonProperty("updateAccountId")
    private String updateAccountId;
    @JsonProperty("updated")
    private String updated;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 3910706009734129751L;

    @JsonProperty("flagName")
    public String getFlagName() {
        return flagName;
    }

    @JsonProperty("flagName")
    public void setFlagName(String flagName) {
        this.flagName = flagName;
    }

    public OusFlag withFlagName(String flagName) {
        this.flagName = flagName;
        return this;
    }

    @JsonProperty("flagValue")
    public String getFlagValue() {
        return flagValue;
    }

    @JsonProperty("flagValue")
    public void setFlagValue(String flagValue) {
        this.flagValue = flagValue;
    }

    public OusFlag withFlagValue(String flagValue) {
        this.flagValue = flagValue;
        return this;
    }

    @JsonProperty("updateAccountId")
    public String getUpdateAccountId() {
        return updateAccountId;
    }

    @JsonProperty("updateAccountId")
    public void setUpdateAccountId(String updateAccountId) {
        this.updateAccountId = updateAccountId;
    }

    public OusFlag withUpdateAccountId(String updateAccountId) {
        this.updateAccountId = updateAccountId;
        return this;
    }

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public OusFlag withUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public OusFlag withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("flagName", flagName).append("flagValue", flagValue).append("updateAccountId", updateAccountId).append("updated", updated).append("additionalProperties", additionalProperties).toString();
    }

}
