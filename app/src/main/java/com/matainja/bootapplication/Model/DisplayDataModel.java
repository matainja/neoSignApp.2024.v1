package com.matainja.bootapplication.Model;

public class DisplayDataModel {
    String id, app_id, queue_id, counter_id, created_at;

    public DisplayDataModel(String id, String app_id, String queue_id, String counter_id, String created_at) {
        this.id = id;
        this.app_id = app_id;
        this.queue_id = queue_id;
        this.counter_id = counter_id;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getQueue_id() {
        return queue_id;
    }

    public void setQueue_id(String queue_id) {
        this.queue_id = queue_id;
    }

    public String getCounter_id() {
        return counter_id;
    }

    public void setCounter_id(String counter_id) {
        this.counter_id = counter_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


}
