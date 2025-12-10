# Getting Ready: The Amazon Online Shopping System  
Understand the Amazon problem and learn the questions to further simplify this problem.

## Problem definition  
Amazon is an online retail platform that allows its users to buy from and sell products to other users. There are numerous product categories that customers can search and choose from. Products have reviews and ratings that can help customers make a buying decision. Each customer has a shopping cart where they can add products they wish to order. Once customers have added their favorite products to the cart, they can check out the cart and choose a payment method to place their orders. The customers are notified about their order and shipment statuses until the order is delivered. The system also allows users to add products they want to sell.

<span style="background-color: yellow; color: blue;">in depth(“Problem Definition for Amazon-like Online Retail Platform.), <a href="./deapth/Problem-Definition-Amazon-Online-Retail-Platform.md">click here</a></span>

# Expectations from the interviewee  
Numerous components are present in the Amazon online shopping system, each with specific constraints and requirements. The following provides an overview of some of the main things the interviewer will want to hear you discuss in more detail.

## Discoverability  
For an online shopping system like Amazon, discoverability is one of the key features that distinguishes it from others. You can ask the following questions to know more about the system:

* How will the buyer discover a product?
* How will the search surface result?

## Cart and checkout  
One of the most significant attributes of the online shopping system is the cart and checkout functionality that it provides to its customers. The interviewer expects the cart and checkout to behave in a certain way. You may ask the interviewer:

* How will the design adhere to known best practices while introducing innovative checkout semantics like "one-click purchase?"

## Payment methods  
The interviewer expects you to ask about the payment methods to determine the necessary requirements. You may ask the following questions:

* How will the users pay? Will they use credit cards, gift cards, and so on?
* How will the payment method work with the checkout process?

## Product reviews and ratings  
Product reviews and ratings help customers make informed decisions regarding what to buy. Knowing how to make this feature an effective part of the system is important. You can ask the interviewer:

* When can a user post a review and a rating?
* How are useful reviews tracked and less useful reviews deprioritized?

<span style="background-color: yellow; color: blue;">in depth(Expectations from the Interviewee), <a href="./deapth/Expectations-from-the-Interviewee.md">click here</a></span>


# Design approach  
We will design this online shopping system using the bottom-up design approach. For this purpose, we will follow the steps below:

* Identify and design the smallest components first, such as a product.
* Use these small components to design bigger components, like a product category that contains different products.
* Repeat the steps above until we design the whole system containing multiple product categories and users.

## Design pattern  
During an interview, it is always good practice to discuss the design patterns under which the Amazon online shopping system falls. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design approach and Design pattern ), <a href="./deapth/Designapproach_pattern.md">click here</a></span>
