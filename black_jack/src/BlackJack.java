import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceSum;

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceSum;

    //window and cards
    int windowHeight = 600;
    int windowWidth = 800;

    int cardHeight = 154;
    int cardWidth = 110;

    JFrame frame = new JFrame("BlackJack");
    JPanel panel = new JPanel(){;
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            try{
            //draw dealer's hand
            //draw hidden card
            Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
            if(!stayButton.isEnabled()){
                hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImageLabel())).getImage();
            }
            g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

            //draw unhidden card
            for(int i = 0; i<dealerHand.size(); i++){
                Card card = dealerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource(card.getImageLabel())).getImage();
                g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)* i, 20, cardWidth, cardHeight, null);
            }

            //draw player's hand
            for(int i = 0; i<playerHand.size(); i++){
                Card card = playerHand.get(i);
                Image cardImg = new ImageIcon(getClass().getResource(card.getImageLabel())).getImage();
                g.drawImage(cardImg, 20 + (cardWidth + 5)* i, 320, cardWidth, cardHeight, null);
            }
            
            if(!stayButton.isEnabled()){
                playerSum = reducePlayerAce();
                dealerSum = reduceDealerAce();
                String message = "";
                boolean playerWins = false;
                if(playerSum>21){
                    message = "you lose.";
                    playerWins = false;
                }
                else if(dealerSum > 21){
                    message = "you win!";
                    playerWins = true;
                }
                else if(playerSum == dealerSum){
                    message = "you tie.";
                }
                else if(playerSum > dealerSum){
                    message = "you win!";
                    playerWins = true;
                }
                else{
                    message = "you lose.";
                    playerWins = false;
                }

                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.WHITE);
                g.drawString(message, 330, 270);
                updateMoney(playerWins);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("hit");
    JButton stayButton = new JButton("stay");
    JButton resetButton = new JButton("reset");
    JButton betButton = new JButton("bet");
    JButton chooseBetButton = new JButton("Bet");

    //intialize money amount and bet amount
    int money = 1000;
    int bettingAmount = 0;


    private class Card{
        String value;
        String suite;

        Card(String value, String suite){
            this.value = value;
            this.suite = suite;
        }
        public String toString(){//this is so we don't return addresses when we call the card class
            return value + "-" + suite;
        }
        public int getValue() {
            if("J".equals(value) || "Q".equals(value) || "K".equals(value)){
                return 10;
            }
            else if("A".equals(value)){
                    return 11;
                }
                else{
                return Integer.parseInt(value);
                }
        }
        public boolean isAce() {
            return "A".equals(value);
        }

        public String getImageLabel(){
            return "./cards/" + toString() + ".png";
        }
    }


    ArrayList<Card> deck = new ArrayList<>();

    public void buildDeck(){
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] suites = {"C", "D", "H", "S"};

        deck.clear();

        for(String suite : suites){ //for each suite in the suites array
            for(String value : values){//for each value in the values array
                Card cards = new Card(value, suite);
                deck.add(cards);
            }
        }
        System.out.println("built deck: ");
        System.out.println(deck);
    }

    public void Shuffle(){
        Random random = new Random();
        for(int i = deck.size()-1; i>0; i--){
            int j = random.nextInt(i+1);

            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
        System.out.println("shuffled deck: ");
        System.out.println(deck);
    }

    public int reducePlayerAce(){
        while(playerSum > 21 && playerAceSum > 0){
            playerAceSum-=1;
            playerSum -= 10;
        }
        return playerSum;
    }

    public int reduceDealerAce(){
        while(dealerSum > 21 && dealerAceSum > 0){
            dealerSum-=10;
            dealerAceSum-=1;
        }
        return dealerSum;
    }

    public void restartGame(){
        deck.clear();
        playerHand.clear();
        dealerHand.clear();
        stayButton.setEnabled(true);
        hitButton.setEnabled(true);
        startGame();
        panel.repaint();
    }

    public void updateMoney(boolean playerWins){
        if(playerWins){
            money = money + bettingAmount + (bettingAmount*2/3);
        }else{
            money = money - bettingAmount;
        }
    }
    public void startGame(){
        buildDeck();
        Shuffle();

        //dealer hand
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceSum = 0;
            //hidden card
            hiddenCard = deck.remove(deck.size()-1);
            dealerSum += hiddenCard.getValue();
            dealerAceSum += hiddenCard.isAce() ? 1:0;
            System.out.println("Dealer hand: ");
            System.out.println(dealerHand);
            System.out.println("Hidden card: " + hiddenCard);

            //non-hidden card
            Card card = deck.remove(deck.size()-1);
            dealerSum += card.getValue();
            dealerAceSum += card.isAce() ? 1:0;
            dealerHand.add(card);

        //player hand
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceSum = 0;
        for(int i =0; i<2; i++){
            card = deck.remove(deck.size()-1);
            playerSum += card.getValue();
            playerAceSum += card.isAce()? 1:0;
            playerHand.add(card);
        }
        System.out.println("Player hand: ");
        System.out.println(playerHand);
    }

    BlackJack(){
        startGame();

        //window
        frame.setVisible(true);
        frame.setSize(windowWidth, windowHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //panel
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(202,22,22));
        frame.add(panel);
            //buttons
        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        resetButton.setFocusable(false);
        buttonPanel.add(resetButton);
        betButton.setFocusable(false);
        buttonPanel.add(betButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        //add button actions
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceSum += card.isAce()? 1:0;
                playerHand.add(card);
                if(reducePlayerAce()>21){
                hitButton.setEnabled(false);
                }
                panel.repaint();
            }
        });
        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while(dealerSum<17){
                    Card card = deck.remove(deck.size()-1);
                    dealerSum += card.getValue();
                    dealerAceSum += card.isAce()? 1:0;
                    dealerHand.add(card);
                }
                panel.repaint();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            restartGame();
            }
        });
        betButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            //betting window
                JFrame betFrame = new JFrame("Money");
                JPanel betPanel = new JPanel();
                betFrame.setVisible(true);
                betFrame.setSize(300, 300);
                betFrame.setLocationRelativeTo(null);
                betFrame.setResizable(false);
                betFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //betting panel 
                betPanel.setLayout(new BorderLayout());
                betPanel.setBackground(new Color(192,192,192));
                betFrame.add(betPanel);
            //create text display for money and display money 
                JTextField moneyDisplay = new JTextField("money: $" + money);
                JPanel moneyPanel = new JPanel();
                moneyPanel.add(moneyDisplay);
                betFrame.add(moneyPanel, BorderLayout.NORTH);
            //create betting display
                JPanel chooseBetPanel = new JPanel();
                JLabel enterBet = new JLabel("Enter Bet Amount");
                JTextField betText = new JTextField(10);//makes text field with a size of 10 columns that
                chooseBetPanel.add(enterBet);
                chooseBetPanel.add(betText);
                chooseBetPanel.add(chooseBetButton); //add button initialized globally
                betPanel.add(chooseBetPanel, BorderLayout.CENTER);
            //initialize typeBet and create betting button
                chooseBetButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        JLabel invalidBet = new JLabel("Invalid Bet Amount");
                        try{
                            bettingAmount = Integer.parseInt(betText.getText());
                            if(bettingAmount>0 && bettingAmount<=money){
                                betFrame.dispose();
                            }
                            else{
                                betFrame.add(invalidBet, BorderLayout.SOUTH);
                                betFrame.validate();
                            }
                        }catch(NumberFormatException ex){
                            betFrame.add(invalidBet, BorderLayout.SOUTH);
                            betFrame.validate();
                        }
                    }
                });

            }
        });
        panel.repaint();
    }
}
