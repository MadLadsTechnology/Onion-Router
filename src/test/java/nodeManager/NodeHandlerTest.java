package nodeManager;

import crypto.RSAKeyPairGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Base64;

public class NodeHandlerTest {
    @Test
    public void testGenerateCircuit() throws NoSuchAlgorithmException, InvalidKeySpecException {
        ArrayList<String> keys = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
            byte[] byte_pubkey = keyPairGenerator.getPublicKey().getEncoded();
            String str_key = Base64.getEncoder().encodeToString(byte_pubkey);
            keys.add(str_key);
        }
        NodeHandler nodeHandler = new NodeHandler(keys);
        PublicKey[] publicKeys = nodeHandler.generateCircuit(3);
        assert(publicKeys.length == 3);
    }
}