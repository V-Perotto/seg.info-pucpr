package Criptografia.Simetrica;

import java.util.Random;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Principal {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
		Pessoa bob = new Pessoa("bob", "bolabolabolabola");
		Pessoa alice = new Pessoa("alice","bolobolobolobolo");
		
		KDC kdc = new KDC(bob, alice);
		
		Random gerador = new Random();
		/* PASSO 1 */
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
		// Identificador
		String p1 = bob.getID();
		// Identificador cifrado na k_bob
		byte[] p2 = AES.cifra(bob.getID(), bob.getChaveMestre());
		// Alice cifrado na k_bob
		byte[] p3 = AES.cifra(alice.getID(), bob.getChaveMestre());	

		// // // // // // // // // // // // // // // // // // // // // // //
		
		
		/* PASSO 2 */
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
		kdc.gerarChaveSessao(p1, p2, p3);
		kdc.getKSCifradaBob();
		
		// Envia para o Bob a Key Session, Bob decifra.
		String KS_Bob = AES.decifra(kdc.getKSCifradaBob(), bob.getChaveMestre());
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
		
		/* PASSO 3 */
		
		// Bob cifra Key Session e envia para o kdc para encaminhar para a Alice
		byte[] p4 = AES.cifra(KS_Bob, alice.getChaveMestre());
		
		// Alice recebe a Key Session cifrada e decifra
		String KS_Alice = AES.decifra(p4, alice.getChaveMestre());
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
		
		/* PASSO 4 */
		
		// Alice gera um número de única utilização e cifra ele com a Key Session
		int nonce = 0;
		
		for (int i = 0; i < 1; i++) {
            nonce = gerador.nextInt(10);
         }
		
		String nonce_A = Integer.toString(nonce);
		
		byte[] p5 = AES.cifra(nonce_A, KS_Alice);

		// Bob decifra o nonce recebido
		String nonce_Bs = AES.decifra(p5, KS_Bob);
		System.out.println(nonce_Bs);
		
		int nonce_B = Integer.parseInt(nonce_Bs);
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
		
		/* PASSO 5 */
		
		// Bob combina uma funcao com a Alice para gerar um novo nonce
		int nonce_C = funcaoAutenticacao(nonce_B);
		
		String NEW_nonce = Integer.toString(nonce_C);
		
		// Bob cifra o novo nonce e envia para a Alice
		byte[] p6 = AES.cifra(NEW_nonce, KS_Bob);
		
		// Alice decifra o novo nonce
		String novoNonce = AES.decifra(p6, KS_Alice);
		System.out.println(novoNonce);
		
		int novoNonceInt = Integer.parseInt(novoNonce);
		
		// Alice compara o novoNonce
		int nonceBob = funcaoAutenticacao(nonce_B);
		
		if (novoNonceInt == nonceBob) {
			System.out.println("é o Bob!");
			// Alice manda mensagem ao Bob
			byte[] p7 = AES.cifra("Oi Bob", KS_Alice);
			
			// Bob decifra mensagem e responde Alice
			String p8 = AES.decifra(p7, KS_Bob);
			
			byte[] p9 = AES.cifra("oi", KS_Bob);
		}
		else {
			System.out.println("Não é o Bob!");
		}
		
		// // // // // // // // // // // // // // // // // // // // // // //
		
	}
	
	public static int funcaoAutenticacao(int var) {
		return var * 45 / 92; 
	}
}

