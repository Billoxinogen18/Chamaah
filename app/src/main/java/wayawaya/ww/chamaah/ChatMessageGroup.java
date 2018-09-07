package wayawaya.ww.chamaah;

/**
 * Created by teddyogallo on 05/07/2018.
 */

public class ChatMessageGroup {

    private long id;
    private boolean isMe;
    private String message;
    private String msgType;
    private String imageData;
    private Long userId;
    private String dateTime;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public boolean getIsme() {
        return isMe;
    }
    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    //for message type

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgtype) {
        this.msgType = msgtype;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imagedata) {
        this.imageData = imagedata;
    }

    //end for message type

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
}
