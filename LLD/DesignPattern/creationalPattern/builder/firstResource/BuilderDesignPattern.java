package LLD.DesignPattern.creationalPattern.builder.firstResource;
public class BuilderDesignPattern{

   public static void main(String args[]){
      User user = new User.UserBuilder()
                .setUserEmail("abc@gmail.com")
                .setUserName("Test")
                .setUserId("10")
                .build();

      System.out.print(user);


      /*
       User user2 = User.UserBuilder.builder()
                .setUserEmail("abc@gmail.com")
                .setUserName("Test")
                .setUserId("10")
                .build();
         System.out.print(user2);
      */
   }
}