package LLD.DesignPattern.creationalPattern.builder.firstResource;

class User{
    private final String userId;
    private final String userName;
    private final String emailId;

    private User(UserBuilder builder){
        //intialize
        this.userId = builder.userId;
        this.userName = builder.userName;
        this.emailId = builder.emailId;
    }

    public String getUserId(){
        return userId;
    }
    public String getUserName(){
        return userName;
    }
    public String getUserEmail(){
        return emailId;
    }

    //inner class to create object 
    static class UserBuilder{

        private String userId;
        private String userName;
        private String emailId;

        public UserBuilder(){

        }

        public UserBuilder setUserId(String userId){
          this.userId = userId;
           return this;
        }
        public UserBuilder setUserName(String userName){
            this.userName = userName;
            return this;
        }
        public UserBuilder setUserEmail(String emailId){
            this.emailId =  emailId;
            return this;
        }

        public User build(){
            User user = new User(this);
            return user;
        }

        /*

        public UserBuilder builder(){
             return new UserBuilder();
        }

        */
    }




}


