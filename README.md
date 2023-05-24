# Component Factory Popup for Vaadin 23+

This is server-side component of [&lt;vcf-popup&gt;](https://github.com/vaadin-component-factory/vcf-popup) Web Component.
It provides a popup that can be bound to element by id, and then be opened by clicking on target element. 

[Live Demo ↗](https://incubator.app.fi/popup-demo/popup)

<img src="https://raw.githubusercontent.com/vaadin/incubator-popup/master/screenshot.png" width="500" alt="Screenshot of vcf-popup">

## Usage
After creating new Popup object it should be bound to a target element by calling the method
`setFor(id)` with the id of the target element as parameter. Then after clicking on the target element, the popup will be opened. 
Clicking outside of the popup will close it.
 
```
    Popup popup = new Popup();
    popup.setFor("id-of-target-element");
    Div text = new Div();
    text.setText("element 1");
    Div text2 = new Div();
    text2.setText("element 2");
    popup.add(text, text2);
```
If the parameter `closeOnClick` is set to `true`, the popup will be closed also after clicking on the popup.
Opening and closing of the popup can be done programmatically by calling the methods `show()` and `hide()`.
```
    Button button = new Button("Show/Hide");
    button.addClickListener(e -> {
        if (popup.isOpened()) {
            popup.hide();
        } else {
            popup.show();
        }
    });
```  
Setting the parameter `opened` to `true` will open the popup. In case the popup is not yet rendered, it will be opened after rendering.
```
    popup.setOpened(true);
```

Popup can be set modal or modeless via the `setModeless()` method.
```
    popup.setModeless(true);
```

If modeless, you can also set up the Popup to close automatically when the content outside the Popup scrolls (handy when used in Grids, for example).
```
    popup.setModeless(true);
    popup.setCloseOnScroll(true);
```

There are convenient methods to set the Popup header title, to add components to the header or the footer:
```
    popup.setHeaderTitle("This is title");
    ...
    popup.getHeader().add(closeBtn);
    ...
    popup.getFooter().add(cancelBtn, applyBtn);
```

## Demo
To run the demo, go to `popup-demo/` subfolder and run `mvn jetty:run`.
After server startup, you'll be able find the demo at [http://localhost:8080/popup](http://localhost:8080/popup)


## Setting up for development:
Clone the project in GitHub (or fork it if you plan on contributing)
```
https://github.com/vaadin-component-factory/popup
```
To build and install the project into the local repository, run 
```mvn install ```


# License & Author

Apache Licence 2
