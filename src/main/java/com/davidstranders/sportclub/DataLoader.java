package com.davidstranders.sportclub;

import com.davidstranders.sportclub.model.*;
import com.davidstranders.sportclub.repository.*;
import com.davidstranders.sportclub.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DS on 5-2-2017.
 */
@Component
public class DataLoader {

    private CompetitionRepository competitionRepository;
    private GameRepository gameRepository;
    private LocationRepository locationRepository;
    private MemberServiceImpl memberService;
    private RoleRepository roleRepository;
    private TeamRepository teamRepository;

    private static final byte[] BERT = parseImage("bert");
    private static final byte[] SS = parseImage("ss");
    private static final byte[] SS2 = parseImage("ss2");
    private static final byte[] ERNIE = parseImage("ernie");

    @Autowired
    public DataLoader(CompetitionRepository competitionRepository, GameRepository gameRepository, LocationRepository locationRepository, MemberServiceImpl memberService, RoleRepository roleRepository, TeamRepository teamRepository) {
        this.competitionRepository = competitionRepository;
        this.gameRepository = gameRepository;
        this.locationRepository = locationRepository;
        this.memberService = memberService;
        this.roleRepository = roleRepository;
        this.teamRepository = teamRepository;
    }

 // @PostConstruct
    private void loadData(){

        // Creating Roles

        Role user = new Role("ROLE_USER");
        user = roleRepository.save(user);
        Role admin = new Role("ROLE_ADMIN");
        admin = roleRepository.save(admin);

        // Creating members

        Member David = new Member("David", "Stranders", "dstranders@hotmail.com", "123", "123", "I'm the boss!", parseDate("18/04/2005"), null);
        David.getRoles().add(admin);
        David.setImage(BERT);
        David = memberService.save(David);

        Member JanJansen = new Member("Jan", "Jansen", "jan.jansen@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("21/10/2013"), null);
        JanJansen.getRoles().add(user);
        JanJansen.setImage(SS);
        JanJansen = memberService.save(JanJansen);

        Member KeesKeessen = new Member("Kees", "Keessen", "kees.keessen@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/02/2006"), null);
        KeesKeessen.getRoles().add(user);
        KeesKeessen.setImage(SS);
        KeesKeessen = memberService.save(KeesKeessen);

        Member KlaasKlaassen = new Member("Klaas", "Klaassen", "klaas.klaassen@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/08/2016"), null);
        KlaasKlaassen.getRoles().add(user);
        KlaasKlaassen = memberService.save(KlaasKlaassen);

        Member PietPietersen = new Member("Piet", "Pietersen", "piet.pietersen@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/04/2010"), null);
        PietPietersen.getRoles().add(user);
        PietPietersen.setImage(SS2);
        PietPietersen = memberService.save(PietPietersen);

        Member AnneDeVries = new Member("Anne", "De Vries", "anne.devries@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2014"), null);
        AnneDeVries.getRoles().add(user);
        AnneDeVries.setImage(SS);
        AnneDeVries = memberService.save(AnneDeVries);

        Member KarelDeJong = new Member("Karel", "De Jong", "karel.dejong@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/11/2008"), null);
        KarelDeJong.getRoles().add(user);
        KarelDeJong.setImage(BERT);
        KarelDeJong = memberService.save(KarelDeJong);

        Member MarieJansen = new Member("Marie", "Jansen", "marie.jansen@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/08/2011"), null);
        MarieJansen.getRoles().add(user);
        MarieJansen.setImage(SS2);
        MarieJansen = memberService.save(MarieJansen);

        Member PeterPetersen = new Member("Peter", "Petersen", "peter.petersen@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2012"), null);
        PeterPetersen.getRoles().add(user);
        PeterPetersen.setImage(ERNIE);
        PeterPetersen = memberService.save(PeterPetersen);

        Member TomTomason = new Member("Tom", "Tomason", "tom.tomason@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/04/2010"), null);
        TomTomason.getRoles().add(user);
        TomTomason = memberService.save(TomTomason);

        Member JanDeJong = new Member("Jan", "De Jong", "jan.dejong@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2015"), null);
        JanDeJong.getRoles().add(user);
        JanDeJong.setImage(ERNIE);
        JanDeJong = memberService.save(JanDeJong);

        Member DaanBakker = new Member("Daan", "Bakker", "daan.bakker@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/03/2014"), null);
        DaanBakker.getRoles().add(user);
        DaanBakker.setImage(SS2);
        DaanBakker = memberService.save(DaanBakker);

        Member EmmaBakker = new Member("Emma", "Bakker", "emma.bakker@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/03/2014"), null);
        EmmaBakker.getRoles().add(user);
        EmmaBakker.setImage(SS);
        EmmaBakker = memberService.save(EmmaBakker);

        Member LotteSmit = new Member("Lotte", "Smit", "lotte.smit@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/01/2017"), null);
        LotteSmit.getRoles().add(user);
        LotteSmit.setImage(SS);
        LotteSmit = memberService.save(LotteSmit);

        Member FleurVisser = new Member("Fleur", "Visser", "fleur.visser@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/09/2016"), null);
        FleurVisser.getRoles().add(user);
        FleurVisser.setImage(BERT);
        FleurVisser = memberService.save(FleurVisser);

        Member LisaVandeBerg = new Member("Lisa", "Van der Berg", "lisa.vanderberg@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/03/2008"), null);
        LisaVandeBerg.getRoles().add(user);
        LisaVandeBerg = memberService.save(LisaVandeBerg);

        Member FleurHendriks = new Member("Fleur", "Hendriks", "fleur.hendriks@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2013"), null);
        FleurHendriks.getRoles().add(user);
        FleurHendriks.setImage(ERNIE);
        FleurHendriks = memberService.save(FleurHendriks);

        Member DaanSmit = new Member("Daan", "Smit", "daan.smit@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/11/2015"), null);
        DaanSmit.getRoles().add(user);
        DaanSmit.setImage(BERT);
        DaanSmit = memberService.save(DaanSmit);

        Member TimVanDijk = new Member("Tim", "Van Dijk", "tim.vandijk@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2012"), null);
        TimVanDijk.getRoles().add(user);
        TimVanDijk = memberService.save(TimVanDijk);

        Member JuliaDeBoer = new Member("Julia", "De Boer", "julia.deboer@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2015"), null);
        JuliaDeBoer.getRoles().add(user);
        JuliaDeBoer.setImage(SS);
        JuliaDeBoer = memberService.save(JuliaDeBoer);

        Member LarsMulder = new Member("Lars", "Mulder", "lars.mulder@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/12/2015"), null);
        LarsMulder.getRoles().add(user);
        LarsMulder.setImage(ERNIE);
        LarsMulder = memberService.save(LarsMulder);

        Member LotteVisser = new Member("Lotte", "Visser", "lotte.visser@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2009"), null);
        LotteVisser.getRoles().add(user);
        LotteVisser.setImage(SS);
        LotteVisser = memberService.save(LotteVisser);

        Member ThijsPeters = new Member("Thijs", "Peters", "thijs.peters@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2015"), null);
        ThijsPeters.getRoles().add(user);
        ThijsPeters.setImage(BERT);
        ThijsPeters = memberService.save(ThijsPeters);

        Member SophieDekker = new Member("Sophie", "Dekker", "sophie.dekker@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/10/2015"), null);
        SophieDekker.getRoles().add(user);
        SophieDekker.setImage(SS2);
        SophieDekker = memberService.save(SophieDekker);

        Member SemDeGroot = new Member("Sem", "De Groot", "sem.degroot@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2015"), null);
        SemDeGroot.getRoles().add(user);
        SemDeGroot = memberService.save(SemDeGroot);

        Member LisaBos = new Member("Lisa", "Bos", "lisa.bos@hotmail.com", "123", "123", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et\n" +
                " dolore magna aliqua.", parseDate("01/06/2015"), null);
        LisaBos.getRoles().add(user);
        LisaBos.setImage(ERNIE);
        LisaBos = memberService.save(LisaBos);

        // Creating competitions

        Competition SummerCompetition = new Competition("Summer Competition", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip exea commodo consequat.", 10, parseDate("01/06/2017"), parseDate("01/08/2017"), "Lorem ipsum dolor sit amet");
        SummerCompetition.setImage(parseImage("summercompetition"));
        SummerCompetition = competitionRepository.save(SummerCompetition);

        Competition WinterCompetition = new Competition("Winter Competition", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip exea commodo consequat.", 10, parseDate("01/11/2017"), parseDate("01/3/2018"), "Lorem ipsum dolor sit amet");
        WinterCompetition = competitionRepository.save(WinterCompetition);

        Competition JuniorCompetition = new Competition("Junior Competition", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip exea commodo consequat.", 10, parseDate("01/03/2017"), parseDate("01/10/2017"), "Lorem ipsum dolor sit amet");
        JuniorCompetition.setImage(parseImage("juniorcompetition"));
        JuniorCompetition = competitionRepository.save(JuniorCompetition);

        // Creating teams

        Team TeamA = new Team("Team A", "Go go go", PietPietersen);
        TeamA.getMembers().add(JanDeJong);
        TeamA.getMembers().add(PietPietersen);
        TeamA.getMembers().add(KlaasKlaassen);
        TeamA.getMembers().add(LisaBos);
        TeamA.getCompetitions().add(SummerCompetition);
        TeamA.getCompetitions().add(JuniorCompetition);
        TeamA.setImage(parseImage("teamA"));
        TeamA = teamRepository.save(TeamA);

        Team TeamB = new Team("Team B", "Yes we can", KarelDeJong);
        TeamB.getMembers().add(KarelDeJong);
        TeamB.getMembers().add(MarieJansen);
        TeamB.getMembers().add(PeterPetersen);
        TeamB.getMembers().add(SophieDekker);
        TeamB.getCompetitions().add(SummerCompetition);
        TeamB.getCompetitions().add(WinterCompetition);
        TeamB.setImage(parseImage("teamB"));
        TeamB = teamRepository.save(TeamB);

        Team TeamC = new Team("Team C", "Yahoo", AnneDeVries);
        TeamC.getMembers().add(AnneDeVries);
        TeamC.getMembers().add(KeesKeessen);
        TeamC.getMembers().add(SemDeGroot);
        TeamC.getMembers().add(LotteVisser);
        TeamC.getCompetitions().add(SummerCompetition);
        TeamC.getCompetitions().add(WinterCompetition);
        TeamC.getCompetitions().add(JuniorCompetition);
        TeamC = teamRepository.save(TeamC);

        Team TeamD = new Team("Team D", "We are the best", ThijsPeters);
        TeamD.getMembers().add(ThijsPeters);
        TeamD.getMembers().add(LarsMulder);
        TeamD.getMembers().add(JuliaDeBoer);
        TeamD.getMembers().add(FleurHendriks);
        TeamD.getCompetitions().add(WinterCompetition);
        TeamD.setImage(parseImage("teamD"));
        TeamD = teamRepository.save(TeamD);

        Team TeamE = new Team("Team E", "", LisaVandeBerg);
        TeamE.getMembers().add(LisaVandeBerg);
        TeamE.getMembers().add(TimVanDijk);
        TeamE.getMembers().add(FleurVisser);
        TeamE.getMembers().add(DaanSmit);
        TeamE.getCompetitions().add(SummerCompetition);
        TeamE.getCompetitions().add(WinterCompetition);
        TeamE.getCompetitions().add(JuniorCompetition);
        TeamE.setImage(parseImage("teamE"));
        TeamE = teamRepository.save(TeamE);


        // Creating locations

        Location FieldA = new Location("Field A", false);
        FieldA = locationRepository.save(FieldA);
        Location FieldB= new Location("Field B", false);
        FieldB = locationRepository.save(FieldB);
        Location FieldC = new Location("Field C", false);
        FieldC = locationRepository.save(FieldC);
        Location FieldD = new Location("Field D", false);
        FieldD = locationRepository.save(FieldD);
        Location FieldE = new Location("Field E", false);
        FieldE = locationRepository.save(FieldE);


        // Creating games Summer Competition

        Game SC_TeamA_TeamB = new Game(6, 4, parseDateTime("24/06/2016 20:00:00"), FieldA, SummerCompetition);
        SC_TeamA_TeamB.getTeams().add(TeamA);
        SC_TeamA_TeamB.getTeams().add(TeamB);
        SC_TeamA_TeamB = gameRepository.save(SC_TeamA_TeamB);

        Game SC_TeamC_TeamE = new Game(4, 4, parseDateTime("24/06/2016 20:00:00"), FieldB, SummerCompetition);
        SC_TeamC_TeamE.getTeams().add(TeamC);
        SC_TeamC_TeamE.getTeams().add(TeamE);
        SC_TeamC_TeamE = gameRepository.save(SC_TeamC_TeamE);

        Game SC_TeamA_TeamC = new Game(3, 5, parseDateTime("30/06/2016 20:00:00"), FieldB, SummerCompetition);
        SC_TeamA_TeamC.getTeams().add(TeamA);
        SC_TeamA_TeamC.getTeams().add(TeamC);
        SC_TeamA_TeamC = gameRepository.save(SC_TeamA_TeamC);

        Game SC_TeamB_TeamE = new Game(3, 3, parseDateTime("30/06/2016 20:00:00"), FieldC, SummerCompetition);
        SC_TeamB_TeamE.getTeams().add(TeamB);
        SC_TeamB_TeamE.getTeams().add(TeamE);
        SC_TeamB_TeamE = gameRepository.save(SC_TeamB_TeamE);

        Game SC_TeamA_TeamE = new Game(8, 2, parseDateTime("06/07/2016 20:00:00"), FieldC, SummerCompetition);
        SC_TeamA_TeamE.getTeams().add(TeamA);
        SC_TeamA_TeamE.getTeams().add(TeamE);
        SC_TeamA_TeamE = gameRepository.save(SC_TeamA_TeamE);

        Game SC_TeamB_TeamC = new Game(4, 6, parseDateTime("06/07/2016 20:00:00"), FieldB, SummerCompetition);
        SC_TeamB_TeamC.getTeams().add(TeamB);
        SC_TeamB_TeamC.getTeams().add(TeamC);
        SC_TeamB_TeamC = gameRepository.save(SC_TeamB_TeamC);

        // Creating games Winter Competition

        Game WC_TeamD_TeamB = new Game(4, 3, parseDateTime("16/12/2016 20:00:00"), FieldB, WinterCompetition);
        WC_TeamD_TeamB.getTeams().add(TeamD);
        WC_TeamD_TeamB.getTeams().add(TeamB);
        WC_TeamD_TeamB = gameRepository.save(WC_TeamD_TeamB);

        Game WC_TeamC_TeamE = new Game(4, 3, parseDateTime("16/12/2016 20:00:00"), FieldD, WinterCompetition);
        WC_TeamC_TeamE.getTeams().add(TeamC);
        WC_TeamC_TeamE.getTeams().add(TeamE);
        WC_TeamC_TeamE = gameRepository.save(WC_TeamC_TeamE);

        Game WC_TeamD_TeamC = new Game(parseDateTime("10/02/2017 20:00:00"), FieldB, WinterCompetition);
        WC_TeamD_TeamC.getTeams().add(TeamD);
        WC_TeamD_TeamC.getTeams().add(TeamC);
        WC_TeamD_TeamC = gameRepository.save(WC_TeamD_TeamC);

        Game WC_TeamB_TeamE = new Game(parseDateTime("10/02/2017 20:00:00"), FieldA, WinterCompetition);
        WC_TeamB_TeamE.getTeams().add(TeamB);
        WC_TeamB_TeamE.getTeams().add(TeamE);
        WC_TeamB_TeamE = gameRepository.save(WC_TeamB_TeamE);

        Game WC_TeamD_TeamE = new Game(parseDateTime("20/02/2017 20:00:00"), FieldE, WinterCompetition);
        WC_TeamD_TeamE.getTeams().add(TeamD);
        WC_TeamD_TeamE.getTeams().add(TeamE);
        WC_TeamD_TeamE = gameRepository.save(WC_TeamD_TeamE);

        Game WC_TeamB_TeamC = new Game(parseDateTime("20/02/2017 20:00:00"), FieldB, WinterCompetition);
        WC_TeamB_TeamC.getTeams().add(TeamB);
        WC_TeamB_TeamC.getTeams().add(TeamC);
        WC_TeamB_TeamC = gameRepository.save(WC_TeamB_TeamC);

        // Creating games Junior Competition

        Game JC_TeamE_TeamA = new Game(parseDateTime("10/04/2017 20:00:00"), FieldA, JuniorCompetition);
        JC_TeamE_TeamA.getTeams().add(TeamE);
        JC_TeamE_TeamA.getTeams().add(TeamA);
        JC_TeamE_TeamA = gameRepository.save(JC_TeamE_TeamA);

        Game JC_TeamE_TeamC = new Game(parseDateTime("10/05/2017 20:00:00"), FieldA, JuniorCompetition);
        JC_TeamE_TeamC.getTeams().add(TeamE);
        JC_TeamE_TeamC.getTeams().add(TeamC);
        JC_TeamE_TeamC = gameRepository.save(JC_TeamE_TeamC);

        Game JC_TeamA_TeamC = new Game(parseDateTime("06/06/2017 20:00:00"), FieldA, JuniorCompetition);
        JC_TeamA_TeamC.getTeams().add(TeamA);
        JC_TeamA_TeamC.getTeams().add(TeamC);
        JC_TeamA_TeamC = gameRepository.save(JC_TeamA_TeamC);


    }

    public static byte[] parseImage(String filename){
        Path path = Paths.get("C:/Users/Dell/Pictures/sportclubapp/" + filename + ".jpg");
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    public static Date parseDate(String ddmmyyyy) {
        java.util.Date utilDate = null;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                return utilDate = formatter.parse(ddmmyyyy);
            } catch (ParseException e) {
                e.printStackTrace();
                return utilDate;
            }
    }

    public static Date parseDateTime(String dateTime) {
        java.util.Date utilDate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            return utilDate = formatter.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return utilDate;
        }
    }

}
