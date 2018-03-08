package spark;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TwitterBean {

    private String username;
    private int followerCount;
    private List<String> hashTags;
    private Date createdDate;
    private String language;
    private boolean isPossiblySensitive;
    private String placeName;
    private String countryName;


    private int favouriteCount;
    private int retweetCount;
    private String text;
    private boolean isRetweet;

    public static TwitterBean createTwitterBean(Status status) {

        TwitterBean tb = new TwitterBean();
        if (status.getUser() != null) {
            tb.username = status.getUser().getName();
            tb.followerCount = status.getUser().getFollowersCount();
        }
        if (status.getPlace() != null) {
            tb.countryName = status.getPlace().getCountry();
            tb.placeName = status.getPlace().getName();
        }
        tb.text = status.getText();
        tb.favouriteCount = status.getFavoriteCount();
        tb.retweetCount = status.getRetweetCount();
        tb.isPossiblySensitive = status.isPossiblySensitive();
        tb.language = status.getLang();
        tb.createdDate = status.getCreatedAt();
        tb.hashTags = new ArrayList<>();
        for (HashtagEntity entity : status.getHashtagEntities()) {
            tb.hashTags.add(entity.getText());
        }
        tb.isRetweet = status.isRetweet();

        return tb;
    }

    public boolean isRetweet() { return isRetweet; }

    public void setRetweet(boolean retweet) { isRetweet = retweet;  }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isPossiblySensitive() {
        return isPossiblySensitive;
    }

    public void setPossiblySensitive(boolean possiblySensitive) {
        isPossiblySensitive = possiblySensitive;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavouriteCount(int favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
