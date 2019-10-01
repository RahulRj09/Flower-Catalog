package flowercatalog;

import java.time.LocalDate;

public class Comment {
    private String name;
    private String comment;
    private LocalDate date;

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

    String getName() {
        return name;
    }

    String getComment() {
        return comment;
    }

    LocalDate getDate() {
        return date;
    }
}
