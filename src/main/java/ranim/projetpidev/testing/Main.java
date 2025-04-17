package ranim.projetpidev.testing;

import ranim.projetpidev.entites.*;
import ranim.projetpidev.services.PromocodeServie;
import ranim.projetpidev.services.UserService;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {


        MyDataBase md = MyDataBase.getInstance();
        PromocodeServie promocodeService = new PromocodeServie();
        Promocode promocode = new Promocode(7,"lion45",14.6, LocalDate.of(2025,8,7),true,6);
        promocodeService.add(promocode);
        // Ajouter 3 codes promo pour l'étudiant avec id_student = 6


    }
}


