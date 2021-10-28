package challenge;

import net.sf.clipsrules.jni.*;

public class Challenge {

    public static void main(String[] args) throws CLIPSException {
        Environment clips = new Environment();
        System.out.println("PERSONAS QUE PUEDEN VOTAR:");
        System.out.println("NOMBRE - EDAD - NACION - VIGENCIA");
        clips.build(
        "(defrule votar"
                + "(age ?a ?n)"
                + "(name ?n)"
                + "(country ?c ?n)"
                + "(current ?v ?n)"
                + "(test (> ?a 18))"
                + "(test (eq ?c \"mex\"))"
                + "(test (> ?v 21))"
                + "=>"
                + "(println ?n \" \"?a\" \"?c \" \"?v))"
        );
        clips.assertString("(name \"Manuel\")");
        clips.assertString("(name \"Jesus\")");
        clips.assertString("(name \"Miguel\")");
        clips.assertString("(name \"Mario\")");
        clips.assertString("(age 21 \"Manuel\")");
        clips.assertString("(age 18 \"Jesus\")");
        clips.assertString("(age 25 \"Miguel\")");
        clips.assertString("(age 28 \"Mario\")");
        clips.assertString("(country \"mex\" \"Manuel\")");
        clips.assertString("(country \"mex\" \"Jesus\")");
        clips.assertString("(country \"arg\" \"Miguel\")");
        clips.assertString("(country \"mex\" \"Mario\")");
        clips.assertString("(current 30 \"Manuel\")");
        clips.assertString("(current 0 \"Jesus\")");
        clips.assertString("(current 0 \"Miguel\")");
        clips.assertString("(current 32 \"Mario\")");
        clips.run();
    }
    
}
