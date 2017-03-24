package com.example.may.msocial.models;

import java.io.Serializable;


public class Protocol implements Serializable {

    private String description;
    private String video;
    private String protocolId;
    private boolean checked;

    public Protocol() {

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getDescription() {

        return description;
    }

    public String getVideo() {
        return video;
    }

    public String getProtocolId() {
        return protocolId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
