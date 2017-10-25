package kz.strixit.ticketmonkey.module;

import com.google.gson.annotations.SerializedName;

/**
 * Created by saken2316 on 10/21/17.
 */

public class EventsModule {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("event_start")
    private String eventStart;
    @SerializedName("portrait_img")
    private String portraitImg;
    @SerializedName("min_price")
    private String minPrice;
//    @SerializedName("tags")
//    private TagModule tags;
//    @SerializedName("place")
//    private String place;

    public EventsModule(int id, String title, String description, String eventStart,
                        String portraitImg, String minPrice, TagModule tags, String place) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventStart = eventStart;
        this.portraitImg = portraitImg;
        this.minPrice = minPrice;
//        this.tags = tags;
//        this.place = place;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getEvent_start() {
        return eventStart;
    }
    public String getPortrait_img() {
        return portraitImg;
    }
    public String getMin_price() {
        return minPrice;
    }
//    public TagModule getTags() {
//        return tags;
//    }
//    public String getPlace() {
//        return place;
//    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }
    public void setPortraitImg(String portraitImg) {
        this.portraitImg = portraitImg;
    }
    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }
//    public void setTags(TagModule tags) {
//        this.tags = tags;
//    }
//    public void setPlace(String place) {
//        this.place = place;
//    }

    @Override
    public String toString() {
        return "EventsModule{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventStart='" + eventStart + '\'' +
                ", portraitImg='" + portraitImg + '\'' +
                ", minPrice='" + minPrice + '\'' +
                '}';
    }
}
