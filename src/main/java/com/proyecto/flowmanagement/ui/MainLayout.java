package com.proyecto.flowmanagement.ui;

import com.proyecto.flowmanagement.ui.views.list.GuideList;
import com.proyecto.flowmanagement.ui.views.list.UserList;
import com.proyecto.flowmanagement.ui.views.pages.GuideCreator;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Flow Management");
        logo.addClassName("logo");

        Anchor logout = new Anchor("login", "Log out");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
header.addClassName("header");
        header.setWidth("100%");
        header.expand(logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Usuarios", UserList.class);
        RouterLink guideLink = new RouterLink("Guide", GuideList.class);
        RouterLink crearGuia = new RouterLink("Crear Guia", GuideCreator.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listLink,
                guideLink,
                crearGuia
//                stepLink
//                new RouterLink("Dashboard", DashboardView.class)
        ));
    }


}
