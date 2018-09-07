package wayawaya.ww.chamaah;

/**
 * Created by teddyogallo on 06/06/2018.
 */

public class chat_summary {

        String title, content, year;
    int id;
    String thumbnail;
    long notificationsNum;

        public chat_summary() {
        }

        public chat_summary(String title, String content, String year,int Idthis, String thumbnail, int notificationsNum) {
            this.title = title;
            this.content = content;
            this.year = year;

            this.id = Idthis;
            this.thumbnail = thumbnail;
            this.notificationsNum = notificationsNum;
        }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
        public String getTitle() {
            return title;
        }

        public void setTitle(String name) {
            this.title = name;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String genre) {
            this.content = genre;
        }

    public Long getnotificationsNum() {
        return notificationsNum;
    }

    public void setnotificationsNum(Long notifinum) {
        this.notificationsNum = notifinum;
    }


}
