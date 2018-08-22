package model;

public class ApartmentResultItem {
    private String title;
    private String location;
    private String date;
    private String price;
    private String link;

    public ApartmentResultItem(String title, String location, String date, String price, String link) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.price = price;
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return link;
    }
}
