package objects;
import it.unisa.dia.gas.jpbc.Element;
import java.util.ArrayList;

//test code for SME
public class SME_Test {
	/*
	public static void main(String[] args) {
		int totalAttributeCnt = 1000;
		// TODO Auto-generated method stub
		SecurityParam sp = (new transSP(totalAttributeCnt)).toSP();

		int totalDeposit=10;

		ArrayList<Integer> A1 = Attribute.GetAttributes(1, 2);
		ArrayList<Integer> A2 = Attribute.GetAttributes(2, 3);
		ArrayList<Integer> A3 = Attribute.GetAttributes(3, 4);

		String code1 = "100001";
		String code2 = "100002";
		String code3 = "100003";
		
		ArrayList<Integer> G = new ArrayList<Integer>();
		ArrayList<Integer> oldG = new ArrayList<Integer>();
		Element oldSk = sp.pairing.getZr().newElement(1);
		Element PG2 = sp.g;
		
		G.addAll(A2);
		
		SMDecrypt smd = new SMDecrypt(sp, G);
		Element PG=smd.genToken();
		Element AP=smd.GenAProof(totalAttributeCnt, totalDeposit);
		Element RP=smd.GenRProof(oldG, oldSk);
		
		SMEncrypt sme = new SMEncrypt(sp, PG);
		if (!sme.AVerify(AP, totalAttributeCnt, totalDeposit)) {
			System.out.println("AVerify failed.");
		} else {
			System.out.println("AVerify OK.");
		}
		if (!sme.RVerify(RP, PG2)) {
			System.out.println("RVerify failed.");
		} else {
			System.out.println("RVerify OK.");
		}
		
		CipherText c1=sme.Encrypt(sp.pairing.getGT().newElement(Integer.parseInt(code1)), A1);
		CipherText c2=sme.Encrypt(sp.pairing.getGT().newElement(Integer.parseInt(code2)), A2);
		CipherText c3=sme.Encrypt(sp.pairing.getGT().newElement(Integer.parseInt(code3)), A3);
		
		Element d1=smd.Decrpyt(c1);
		Element d2=smd.Decrpyt(c2);
		Element d3=smd.Decrpyt(c3);
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
	}
	//*/
}
