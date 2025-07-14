package com.coffeeshop.coffee_shop.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")

public class CoffeeShop extends VerticalLayout{
    private static int coffeeStock = 100;
    private static int cookieStock = 100;
    private static double profit;
    private int coffee = 0;
    private int cookies = 0;
    private TextField coffeeField;
    private TextField cookieField;
    public CoffeeShop() {
        setAlignItems(Alignment.CENTER);
        userInput(this);
    }
    public void userInput(VerticalLayout layout) {
        Span welcomeLabel = new Span("Welcome to the Coffee and Cookie Shop!");
        Span priceLabel = new Span("We have coffee for $2.50 each and cookies for $1.50 each.");

        Span coffeeLabel = new Span("How many coffees would you like to buy?");
        this.coffeeField = new TextField();
        
        Span cookieLabel = new Span("How many cookies would you like to buy?");
        this.cookieField = new TextField();

        Button submitButton = new Button("Add to Cart");
        submitButton.addClickListener(event -> {
            try {
                int coffeeCount = Integer.parseInt(coffeeField.getValue());
                int cookieCount = Integer.parseInt(cookieField.getValue());
                addItemsToCart(coffeeCount, cookieCount, layout);
            } catch (NumberFormatException ex) {
                Notification.show("Please enter valid numbers for coffee and cookies.");
            }
        });
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(click -> {
            coffee = 0;
            cookies = 0;
            coffeeField.clear();
            cookieField.clear();
            Notification.show("Cart cleared.");
        });
        layout.add(welcomeLabel, priceLabel, coffeeLabel, coffeeField, cookieLabel, cookieField, submitButton, cancelButton);
    }
    public void addItemsToCart(int coffeeCount, int cookieCount, VerticalLayout layout) {
        if (coffeeCount < 0 || cookieCount < 0) {
            Notification.show("Please enter non-negative numbers.");
            return;
        }
        if (coffeeCount > coffeeStock) {
            Notification.show("Not enough coffee in stock. Available: " + coffeeStock);
            coffeeCount = coffeeStock;
        }
        if (cookieCount > cookieStock) {
            Notification.show("Not enough cookies in stock. Available: " + cookieStock);
            cookieCount = cookieStock;
        }

        coffee += coffeeCount;
        cookies += cookieCount;

        String message;
        double totalCost = (coffee * 2.5) + (cookies * 1.5);
        if (coffee > 0 && cookies == 0) {
            message = "You have " + coffee + " coffee(s) in your cart for a total of $" + totalCost;
        } else if (cookies > 0 && coffee == 0) {
            message = "You have " + cookies + " cookie(s) in your cart for a total of $" + totalCost;
        } else if (coffee > 0 && cookies > 0) {
            message = "You have " + coffee + " coffee(s) and " + cookies + " cookie(s) in your cart for a total of $" + totalCost;
        } else {
            message = "No items in cart.";
            Notification.show(message);
            return;
        }
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Confirm Purchase");
        dialog.setText(message + "\nWould you like to buy these items?");
        dialog.setCancelable(true);
        dialog.setConfirmText("Yes");
        dialog.setCancelText("No");
        dialog.addConfirmListener(confirm -> {
                coffeeStock -= coffee;
                cookieStock -= cookies;
                profit += totalCost;
                Notification.show("Thank you for your purchase! Total cost: $" + totalCost);
                coffee = 0;
                cookies = 0;
                coffeeField.clear();
                cookieField.clear();
        });
        dialog.addCancelListener(cancel -> {
                Notification.show("Purchase cancelled.");
                coffee = 0;
                cookies = 0;
                coffeeField.clear();
                cookieField.clear();
        });
        dialog.open();
        initializeStock();
    }
    public void initializeStock() {
        if (coffeeStock == 0) {
            coffeeStock = 100;
        }
        if (cookieStock == 0) {
            cookieStock = 100;
        }
    }
}
