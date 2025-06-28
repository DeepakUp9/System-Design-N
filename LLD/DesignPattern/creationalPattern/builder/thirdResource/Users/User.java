package LLD.DesignPattern.creationalPattern.builder.thirdResource.Users;


public class User {
   private String name;
   private String email;
   private String address;
   private String phone;
   private String profilePic;

   private User(UserBuilder userBuilder){
        this.name = userBuilder.name;
        this.email = userBuilder.email;
        this.address = userBuilder.address;
        this.phone = userBuilder.phone;
        this.profilePic = userBuilder.profilePic;
   }

   public String getName() {
    return name;
   }

   public String getEmail() {
    return email;
   }

   public String getAddress() {
    return address;
   }

   public String getPhone() {
    return phone;
   }

   public String getProfilePic() {
    return profilePic;
   }


   @Override
    public String toString() {
       return  this.name + "  " + this.email  + " " + this.address +" "+ this.phone +" " + this.profilePic;
    }


   static class UserBuilder {
        private String name;
        private String email;
        private String address;
        private String phone;
        private String profilePic;

        UserBuilder(){

        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder setProfilePic(String profilePic) {
            this.profilePic = profilePic;
            return this;
        }

        public User build(){
            User user = new User(this);
            return user;
        }
    
   }

}
