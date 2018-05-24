package hometask03;

public class LogParameters {
    private String timestamp;
    private String activity;
    private String loginUsername;
    private String dataObject;
    private String records;
    private String userAction;
    private String userActionStatus;
    private String labels;
    private String serviceType;
    private String mappingIds;
    private String uri;


    public LogParameters() {
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getDataObject() {
        return dataObject;
    }

    public void setDataObject(String dataObject) {
        this.dataObject = dataObject;
    }

    public String getRecords() {
        return records;
    }

    public void setRecords(String records) {
        this.records = records;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getUserActionStatus() {
        return userActionStatus;
    }

    public void setUserActionStatus(String userActionStatus) {
        this.userActionStatus = userActionStatus;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMappingIds() {
        return mappingIds;
    }

    public void setMappingIds(String mappingIds) {
        this.mappingIds = mappingIds;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getActivity() {
        return activity;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void printLogParameterValue() {
        System.out.println(getTimestamp() + getActivity() + getLoginUsername()
                + getDataObject() + getUserAction() + getUserActionStatus()
                + getLabels() + getServiceType() + getMappingIds()
                + getUri() + "\n");
    }
}
