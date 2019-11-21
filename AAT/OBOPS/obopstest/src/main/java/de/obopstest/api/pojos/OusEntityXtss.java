package de.obopstest.api.pojos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
        "ousStatusEntityId",
        "state",
        "substate",
        "ousPartId",
        "ousFlags",
        "projectStatusId",
        "dataReducerUsername",
        "dataReducerArcNode",
        "piPreferredArc",
        "projectPriorityGrade",
        "keywordCode1",
        "keywordCode2",
        "executive",
        "requestedArray",
        "fullyObservedOn",
        "daysSinceFullyObserved",
        "receiverBand",
        "nrQA0PassAsdms",
        "nrQA0SemiPassAsdms",
        "secondsObserved",
        "sbNames",
        "nrSchedBlocks",
        "projectCode"
})
public class OusEntityXtss implements Serializable
{

    @JsonProperty("ousStatusEntityId")
    private String ousStatusEntityId;
    @JsonProperty("state")
    private String state;
    @JsonProperty("substate")
    private Object substate;
    @JsonProperty("ousPartId")
    private String ousPartId;
    @JsonProperty("ousFlags")
    private List<OusFlag> ousFlags = null;
    @JsonProperty("projectStatusId")
    private String projectStatusId;
    @JsonProperty("dataReducerUsername")
    private String dataReducerUsername;
    @JsonProperty("dataReducerArcNode")
    private String dataReducerArcNode;
    @JsonProperty("piPreferredArc")
    private String piPreferredArc;
    @JsonProperty("projectPriorityGrade")
    private String projectPriorityGrade;
    @JsonProperty("keywordCode1")
    private String keywordCode1;
    @JsonProperty("keywordCode2")
    private Object keywordCode2;
    @JsonProperty("executive")
    private String executive;
    @JsonProperty("requestedArray")
    private String requestedArray;
    @JsonProperty("fullyObservedOn")
    private String fullyObservedOn;
    @JsonProperty("daysSinceFullyObserved")
    private String daysSinceFullyObserved;
    @JsonProperty("receiverBand")
    private String receiverBand;
    @JsonProperty("nrQA0PassAsdms")
    private int nrQA0PassAsdms;
    @JsonProperty("nrQA0SemiPassAsdms")
    private int nrQA0SemiPassAsdms;
    @JsonProperty("secondsObserved")
    private String secondsObserved;
    @JsonProperty("sbNames")
    private String sbNames;
    @JsonProperty("nrSchedBlocks")
    private String nrSchedBlocks;
    @JsonProperty("projectCode")
    private String projectCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5948410886070464201L;

    @JsonProperty("ousStatusEntityId")
    public String getOusStatusEntityId() {
        return ousStatusEntityId;
    }

    @JsonProperty("ousStatusEntityId")
    public void setOusStatusEntityId(String ousStatusEntityId) {
        this.ousStatusEntityId = ousStatusEntityId;
    }

    public OusEntityXtss withOusStatusEntityId(String ousStatusEntityId) {
        this.ousStatusEntityId = ousStatusEntityId;
        return this;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    public OusEntityXtss withState(String state) {
        this.state = state;
        return this;
    }

    @JsonProperty("substate")
    public Object getSubstate() {
        return substate;
    }

    @JsonProperty("substate")
    public void setSubstate(Object substate) {
        this.substate = substate;
    }

    public OusEntityXtss withSubstate(Object substate) {
        this.substate = substate;
        return this;
    }

    @JsonProperty("ousPartId")
    public String getOusPartId() {
        return ousPartId;
    }

    @JsonProperty("ousPartId")
    public void setOusPartId(String ousPartId) {
        this.ousPartId = ousPartId;
    }

    public OusEntityXtss withOusPartId(String ousPartId) {
        this.ousPartId = ousPartId;
        return this;
    }

    @JsonProperty("ousFlags")
    public List<OusFlag> getOusFlags() {
        return ousFlags;
    }

    @JsonProperty("ousFlags")
    public void setOusFlags(List<OusFlag> ousFlags) {
        this.ousFlags = ousFlags;
    }

    public OusEntityXtss withOusFlags(List<OusFlag> ousFlags) {
        this.ousFlags = ousFlags;
        return this;
    }

    @JsonProperty("projectStatusId")
    public String getProjectStatusId() {
        return projectStatusId;
    }

    @JsonProperty("projectStatusId")
    public void setProjectStatusId(String projectStatusId) {
        this.projectStatusId = projectStatusId;
    }

    public OusEntityXtss withProjectStatusId(String projectStatusId) {
        this.projectStatusId = projectStatusId;
        return this;
    }

    @JsonProperty("dataReducerUsername")
    public String getDataReducerUsername() {
        return dataReducerUsername;
    }

    @JsonProperty("dataReducerUsername")
    public void setDataReducerUsername(String dataReducerUsername) {
        this.dataReducerUsername = dataReducerUsername;
    }

    public OusEntityXtss withDataReducerUsername(String dataReducerUsername) {
        this.dataReducerUsername = dataReducerUsername;
        return this;
    }

    @JsonProperty("dataReducerArcNode")
    public String getDataReducerArcNode() {
        return dataReducerArcNode;
    }

    @JsonProperty("dataReducerArcNode")
    public void setDataReducerArcNode(String dataReducerArcNode) {
        this.dataReducerArcNode = dataReducerArcNode;
    }

    public OusEntityXtss withDataReducerArcNode(String dataReducerArcNode) {
        this.dataReducerArcNode = dataReducerArcNode;
        return this;
    }

    @JsonProperty("piPreferredArc")
    public String getPiPreferredArc() {
        return piPreferredArc;
    }

    @JsonProperty("piPreferredArc")
    public void setPiPreferredArc(String piPreferredArc) {
        this.piPreferredArc = piPreferredArc;
    }

    public OusEntityXtss withPiPreferredArc(String piPreferredArc) {
        this.piPreferredArc = piPreferredArc;
        return this;
    }

    @JsonProperty("projectPriorityGrade")
    public String getProjectPriorityGrade() {
        return projectPriorityGrade;
    }

    @JsonProperty("projectPriorityGrade")
    public void setProjectPriorityGrade(String projectPriorityGrade) {
        this.projectPriorityGrade = projectPriorityGrade;
    }

    public OusEntityXtss withProjectPriorityGrade(String projectPriorityGrade) {
        this.projectPriorityGrade = projectPriorityGrade;
        return this;
    }

    @JsonProperty("keywordCode1")
    public String getKeywordCode1() {
        return keywordCode1;
    }

    @JsonProperty("keywordCode1")
    public void setKeywordCode1(String keywordCode1) {
        this.keywordCode1 = keywordCode1;
    }

    public OusEntityXtss withKeywordCode1(String keywordCode1) {
        this.keywordCode1 = keywordCode1;
        return this;
    }

    @JsonProperty("keywordCode2")
    public Object getKeywordCode2() {
        return keywordCode2;
    }

    @JsonProperty("keywordCode2")
    public void setKeywordCode2(Object keywordCode2) {
        this.keywordCode2 = keywordCode2;
    }

    public OusEntityXtss withKeywordCode2(Object keywordCode2) {
        this.keywordCode2 = keywordCode2;
        return this;
    }

    @JsonProperty("executive")
    public String getExecutive() {
        return executive;
    }

    @JsonProperty("executive")
    public void setExecutive(String executive) {
        this.executive = executive;
    }

    public OusEntityXtss withExecutive(String executive) {
        this.executive = executive;
        return this;
    }

    @JsonProperty("requestedArray")
    public String getRequestedArray() {
        return requestedArray;
    }

    @JsonProperty("requestedArray")
    public void setRequestedArray(String requestedArray) {
        this.requestedArray = requestedArray;
    }

    public OusEntityXtss withRequestedArray(String requestedArray) {
        this.requestedArray = requestedArray;
        return this;
    }

    @JsonProperty("fullyObservedOn")
    public String getFullyObservedOn() {
        return fullyObservedOn;
    }

    @JsonProperty("fullyObservedOn")
    public void setFullyObservedOn(String fullyObservedOn) {
        this.fullyObservedOn = fullyObservedOn;
    }

    public OusEntityXtss withFullyObservedOn(String fullyObservedOn) {
        this.fullyObservedOn = fullyObservedOn;
        return this;
    }

    @JsonProperty("daysSinceFullyObserved")
    public String getDaysSinceFullyObserved() {
        return daysSinceFullyObserved;
    }

    @JsonProperty("daysSinceFullyObserved")
    public void setDaysSinceFullyObserved(String daysSinceFullyObserved) {
        this.daysSinceFullyObserved = daysSinceFullyObserved;
    }

    public OusEntityXtss withDaysSinceFullyObserved(String daysSinceFullyObserved) {
        this.daysSinceFullyObserved = daysSinceFullyObserved;
        return this;
    }

    @JsonProperty("receiverBand")
    public String getReceiverBand() {
        return receiverBand;
    }

    @JsonProperty("receiverBand")
    public void setReceiverBand(String receiverBand) {
        this.receiverBand = receiverBand;
    }

    public OusEntityXtss withReceiverBand(String receiverBand) {
        this.receiverBand = receiverBand;
        return this;
    }

    @JsonProperty("nrQA0PassAsdms")
    public int getNrQA0PassAsdms() {
        return nrQA0PassAsdms;
    }

    @JsonProperty("nrQA0PassAsdms")
    public void setNrQA0PassAsdms(int nrQA0PassAsdms) {
        this.nrQA0PassAsdms = nrQA0PassAsdms;
    }

    public OusEntityXtss withNrQA0PassAsdms(int nrQA0PassAsdms) {
        this.nrQA0PassAsdms = nrQA0PassAsdms;
        return this;
    }

    @JsonProperty("nrQA0SemiPassAsdms")
    public int getNrQA0SemiPassAsdms() {
        return nrQA0SemiPassAsdms;
    }

    @JsonProperty("nrQA0SemiPassAsdms")
    public void setNrQA0SemiPassAsdms(int nrQA0SemiPassAsdms) {
        this.nrQA0SemiPassAsdms = nrQA0SemiPassAsdms;
    }

    public OusEntityXtss withNrQA0SemiPassAsdms(int nrQA0SemiPassAsdms) {
        this.nrQA0SemiPassAsdms = nrQA0SemiPassAsdms;
        return this;
    }

    @JsonProperty("secondsObserved")
    public String getSecondsObserved() {
        return secondsObserved;
    }

    @JsonProperty("secondsObserved")
    public void setSecondsObserved(String secondsObserved) {
        this.secondsObserved = secondsObserved;
    }

    public OusEntityXtss withSecondsObserved(String secondsObserved) {
        this.secondsObserved = secondsObserved;
        return this;
    }

    @JsonProperty("sbNames")
    public String getSbNames() {
        return sbNames;
    }

    @JsonProperty("sbNames")
    public void setSbNames(String sbNames) {
        this.sbNames = sbNames;
    }

    public OusEntityXtss withSbNames(String sbNames) {
        this.sbNames = sbNames;
        return this;
    }

    @JsonProperty("nrSchedBlocks")
    public String getNrSchedBlocks() {
        return nrSchedBlocks;
    }

    @JsonProperty("nrSchedBlocks")
    public void setNrSchedBlocks(String nrSchedBlocks) {
        this.nrSchedBlocks = nrSchedBlocks;
    }

    public OusEntityXtss withNrSchedBlocks(String nrSchedBlocks) {
        this.nrSchedBlocks = nrSchedBlocks;
        return this;
    }

    @JsonProperty("projectCode")
    public String getProjectCode() {
        return projectCode;
    }

    @JsonProperty("projectCode")
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public OusEntityXtss withProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    public OusEntityXtss withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ousStatusEntityId", ousStatusEntityId).append("state", state).append("substate", substate).append("ousPartId", ousPartId).append("ousFlags", ousFlags).append("projectStatusId", projectStatusId).append("dataReducerUsername", dataReducerUsername).append("dataReducerArcNode", dataReducerArcNode).append("piPreferredArc", piPreferredArc).append("projectPriorityGrade", projectPriorityGrade).append("keywordCode1", keywordCode1).append("keywordCode2", keywordCode2).append("executive", executive).append("requestedArray", requestedArray).append("fullyObservedOn", fullyObservedOn).append("daysSinceFullyObserved", daysSinceFullyObserved).append("receiverBand", receiverBand).append("nrQA0PassAsdms", nrQA0PassAsdms).append("nrQA0SemiPassAsdms", nrQA0SemiPassAsdms).append("secondsObserved", secondsObserved).append("sbNames", sbNames).append("nrSchedBlocks", nrSchedBlocks).append("projectCode", projectCode).append("additionalProperties", additionalProperties).toString();
    }

}
