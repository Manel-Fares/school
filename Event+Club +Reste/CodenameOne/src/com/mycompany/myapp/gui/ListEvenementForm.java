/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.gui;

import com.codename1.components.ImageViewer;
import com.codename1.components.ShareButton;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.mycompany.myapp.Services.DemandespecifiqueService;
import com.mycompany.myapp.Services.ServiceClub;
import com.mycompany.myapp.Services.ServiceEvenement;
import com.mycompany.myapp.Services.ServiceParticipation;
import com.mycompany.myapp.entities.Evenement;
import com.mycompany.myapp.entities.User;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author asus
 */
public class ListEvenementForm extends SideMenuEtudiantForm1 {

    Form current;
    Image imgg = null;
    ImageViewer iv = null;
    EncodedImage ec;

    public ListEvenementForm(Resources res) {
        super(BoxLayout.y());
        //System.out.println("************************voila votre resultats"+ServiceClub.getInstance().getClubResponsable());
        getStyle().setBgColor(0xC7EEF5);
        getStyle().setBgTransparency(255);
        Toolbar tb = getToolbar();
        tb.setTitleCentered(false);
        Button menuButton = new Button("");
        menuButton.setUIID("Title");
        FontImage.setMaterialIcon(menuButton, FontImage.MATERIAL_MENU);
        menuButton.addActionListener(e -> getToolbar().openSideMenu());

        Container titleCmp = BoxLayout.encloseX(
                FlowLayout.encloseIn(menuButton), FlowLayout.encloseIn(
                new Label("Events List", "Title")
        )
        );

        tb.setTitleComponent(titleCmp);
        if (DemandespecifiqueService.getInstance().getTestResp(Integer.parseInt(User.getCurrentId()))) {
            tb.addCommandToOverflowMenu("Demande Event", null, e -> new DemandeEvenementForm(res).show());
            tb.addCommandToOverflowMenu("consult Demande Event", null, e -> new ConsulterDemandeEventform(res).show());
            tb.addCommandToOverflowMenu("your Club", null, e -> new ClubSpecifiqueForm(res).show());
            tb.addCommandToOverflowMenu("your Events", null, e -> new EvenementClubSpecifiqueForm(res).show());
        }
        

        ArrayList<Evenement> Evenements;
        Evenements = ServiceEvenement.getInstance().getEvenement();
        for (Evenement event : Evenements) {
            Container c1 = new Container(new BoxLayout(BoxLayout.Y_AXIS));
            Label l = new Label("" + event.getIdEvenement());
            Label l1 = new Label(event.getDateDebut());
            Label l2 = new Label(event.getDateFin());
            Label l3 = new Label("" + event.getIdClub().getNomClub());
            Label l4 = new Label("" + event.getIdClub().getIdClub());
            l.setVisible(false);
            l3.setVisible(false);
            l4.setVisible(false);
            String url = "http://localhost/projet/schoolMgt/web/images/" + event.getImage();
            int deviceWidth = Display.getInstance().getDisplayWidth();
            Image placeholder = Image.createImage(deviceWidth, deviceWidth / 2, 0xbfc9d2);
            EncodedImage encImage = EncodedImage.createFromImage(placeholder, false);
            imgg = URLImage.createToStorage(encImage, url, url, URLImage.RESIZE_SCALE);
            iv = new ImageViewer(imgg);
            c1.add(iv);
            Container c = new Container(new BoxLayout(BoxLayout.X_AXIS));
            Map<Evenement, Integer> nbrPartEvenements = new HashMap<>();
            nbrPartEvenements = ServiceParticipation.getInstance().getEeventDetail(event.getIdEvenement());
            for (Map.Entry<Evenement, Integer> entry : nbrPartEvenements.entrySet()) {
                try {
                    Image group = Image.createImage("/group.png");
                    Label nbrPart = new Label("Number Participants:" + entry.getValue(), group);
                    c.add(nbrPart);
                } catch (IOException ex) {
                }

            }
            /**
             * *************************
             */
            ShareButton sb = new ShareButton();
            sb.setText("Share");

            //Image screenshot = Image.createImage(getWidth(),getHeight());
            revalidate();
            setVisible(true);
            paintComponent(placeholder.getGraphics(), true);

            String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
            try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
                ImageIO.getImageIO().save(placeholder, os, ImageIO.FORMAT_PNG, 1);
            } catch (IOException err) {
                Log.e(err);
            }
            sb.setImageToShare(imageFile, "image/png");
            /**
             * **************************
             */
            c.add(sb);
            c1.add(c);
            Container c2 = new Container(new BoxLayout(BoxLayout.X_AXIS));
            c2.add(c1);
            add(c2);
            iv.addPointerReleasedListener(e -> new EvenementDetailForm(res, Integer.parseInt(l.getText()), l3.getText(), Integer.parseInt(l4.getText())).show());
            //  part.addActionListener(e -> new EvenementDetailForm(res, Integer.parseInt(l.getText()), l3.getText(), Integer.parseInt(l4.getText())).show());
        }
        //getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());

        setupSideMenu(res);
    }

    private Image colorCircle(int color) {
        int size = Display.getInstance().convertToPixels(3);
        Image i = Image.createImage(size, size, 0);
        Graphics g = i.getGraphics();
        g.setColor(color);
        g.fillArc(0, 0, size, size, 0, 360);
        return i;
    }

}
