package main.java.models;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class Item {

    @BsonProperty(value = "_id")
    private ObjectId _id;
    private String title;
    private String body;
    private Date createdAt;
    private Date updatedAt;

    public ObjectId getId() {
        return _id;
    }

    public Item setId(ObjectId id) {
        this._id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Item setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Item setBody(String body) {
        this.body = body;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Item setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Item setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + _id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(_id, item._id) && Objects.equals(title, item.title) && Objects.equals(body, item.body) && Objects.equals(createdAt, item.createdAt) && Objects.equals(updatedAt, item.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, title, body, createdAt, updatedAt);
    }
}