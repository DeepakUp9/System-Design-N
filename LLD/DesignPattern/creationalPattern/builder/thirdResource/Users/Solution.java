package LLD.DesignPattern.creationalPattern.builder.thirdResource.Users;
public class Solution {
    public static void main(String[] args) {
        User user = new User.UserBuilder().setName("test").setEmail("abc@gmail.com").build();

        System.out.println(user);

    }

}
