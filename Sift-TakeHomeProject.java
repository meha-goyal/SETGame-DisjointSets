import java.util.*;
import java.io.*;
/**
* Author: Meha Goyal
* Date: Jan 5th, 2022
* I have created three classes: Card, SET, and SETGame
* use "java SETGame filname" to run the program
 */

public class Card{
    private String color; // blue, green, yellow
    private char symbol; // A, S, or H
    private String shading; // lower, upper, symbol
    private int num; // 1, 2, 3
    public String card;
    public Card(String col, char sym, String shade, int num){
        color = col;
        symbol = sym;
        shading = shade;
        this.num = num;
    }
    public Card(String card){ // creating a card from an imput string
        this.card = card; // to print later
        color = card.substring(0, card.indexOf(" "));
        num = card.substring(card.indexOf(" ")+1).length;
        symbol = String.charAt(card.indexOf(" ")+1);
        if(symbol == '$' || symbol == 'S' || symbol == 's'){
            symbol = 'S';
        }
        else if(symbol == '@' || symbol == 'A' || symbol == 'a'){
            symbol = 'A';
        }
        else{
            symbol = 'H';
        }
        if(Character.isLetter(symbol)){
            if(Character.isUppercase(symbol)){
                shading = "upper";
            }
            else{
                shading = "lower";
            }
        }
        else{
            shading = "symbol";
        }
    }

    public boolean equals(Card rhs){
        if(!this.color.equals(rhs.color) || this.symbol != rhs.symbol || !this.shading.equals(rhs.shading) || this.num != rhs.num){
            return false;
        }
        return true;
    }
    public String getColor(){
        return color;
    }
    public char getSymbol(){
        return symbol;
    }
    public string getShading(){
        return shading;
    }
    public int getNumber(){
        return num;
    }
}

public class SET{
    private Card[] set = new Card[3];

    public Set(Card one, Card two, Card three){
        set[0] = one;
        set[1] = two;
        set[2] = three;
    }
    public Set(List<Card> tempset){
        set[0] = tempset.get(0);
        set[1] = tempset.get(1);
        set[2] = tempset.get(2);
    }

    public boolean isValidSet(){
        return checkColor() && checkSymbol() && checkShading() && checkNumbers();
    }
    //You can use templating to condense the check functions into one function with a parameter
    public boolean checkColor(){
        String col1 = set[0].getColor();
        String col2 = set[1].getColor();
        String col3 = set[2].getColor();
        if(col1.equals(col2) && col2.equals(col3)){
            return true;
        }
        if(!col1.equals(col2) && !col2.equals(col3) && !col1.equals(col3)){
            return true;
        }
        return false;
    }
    public boolean checkSymbol(){
        char sym1 = set[0].getSymbol();
        char sym2 = set[1].getSymbol();
        char sym3 = set[2].getSymbol();
        if(sym1 == sym2 && sym2 == sym3){
            return true;
        }
        if(sym1 != sym2 && sym2 != sym3 && sym1 != sym3){
            return true;
        }
        return false;
    }
    public boolean checkShading(){
        String shade1 = set[0].getShading();
        String shade2 = set[1].getShading();
        String shade3 = set[2].getShading();
        if(shade1.equals(shade2) && shade2.equals(shade3)){
            return true;
        }
        if(!shade1.equals(shade2) && !shade2.equals(shade3) && !shade1.equals(shade3)){
            return true;
        }
        return false;
    }
    public boolean checkNumbers(){
        int num1 = set[0].getNumber();
        int num2 = set[1].getNumber();
        int num3 = set[2].getNumber();
        if(num1 == num2 && num2 == num3){
            return true;
        }
        if(num1 != num2 && num2!=num3 && num3 != num1){
            return true;
        }
        return false;
    }

    public int[] getCards(){
        return set;
    }
}

public class SETGame{
    private List<SET> res = new ArrayList();

    //generates complete output file
    public void generateSets(List<Card> input){
        List<List<Card>> sets = new ArrayList();
        sets.add(new ArrayList<Card>());
        for(Card c : input){
            List<List<Card>> newSets = new ArrayList();
            for(List<Card> curr : sets){
                newSets.add(new ArrayList<Card>(curr){{add(c);}});
            }
            for(List<Card> set : newSets){
                if(set.size()==3){ // not all sets are valid
                    Set curr = new Set(set);
                    if(curr.isValidSet()) {sets.add(curr);}; // add to
                }
            }
        }
        res = sets;
    }

    public List<SET> generateDisjoint(List<SET> res){
        List<SET> list = new ArrayList();
        Set<Card> set = new HashSet<>();
        for(SET s : res){
            Card[] cards = s.getCards();
            if(!set.contains(cards[0]) && !set.contains(cards[1]) && !set.contains(cards[2])){
                list.add(s);
                set.add(cards[0]);
                set.add(cards[1]);
                set.add(cards[2]);
            }
        }
        return list;
    }


    public void generateOutput(List<String> input){
        generateSets(input);
        FileWriter f = new FileWriter(new File("outfile"));
        int total = res.size();
        List<SET> disjointSets = generateDisjoint(res);
        int disjoint = disjointSets.size();
        f.write(total + "\n");
        f.write(disjoint + "\n");

        for(SET s : disjointSets){
            for(Card c : s.getCards()){
                f.write(c.card + "\n");
            }
            f.write("\n");
        }
        f.close();
    }

    public static void main(String[] args){
        if(args.length < 1) {
            System.out.println("Error, usage: java SETGame inputfile");
            return;
        }
        Scanner reader = new Scanner(new FileInputStream(args[0]));
        int n = reader.nextInt();
        List<Card> input = new ArrayList<>();
        while(reader.hasNext()){
            String s = reader.next();
            s+= " ";
            s+= reader.next();
            input.add(new Card(s)); // uses string based constructor
            s = "";
        }
        generateOutput(input);
    }
}

