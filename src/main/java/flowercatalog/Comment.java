package flowercatalog;

import java.time.LocalDate;

public class Comment {
    String name;
    String comment;
    LocalDate date;

    public Comment(StringBuffer name, StringBuffer comment, LocalDate date) {
        this.name = name.toString();
        this.comment = comment.toString();
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", date=" + date +
                '}';
    }
}
