package sample;

public class Request {
        public Request() {
        }

        public Request(String UserID) {
                this.UserID = UserID;
        }

        public String getUserID() {
                return UserID;
        }

        public void setUserID(String userID) {
                UserID = userID;
        }

        public String UserID;
}
