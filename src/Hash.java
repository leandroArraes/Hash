import java.io.IOException;
import java.nio.file.*;
import java.security.*;

public class Hash {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public String doHash(String nomeArquivo) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] content = Files.readAllBytes(Paths.get(nomeArquivo));
        byte[] digest = md.digest(content);
        return bytesToHex(digest).toLowerCase();
    }

    public String doHashInFolder(String folderPath) throws NoSuchAlgorithmException, IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath))) {
            StringBuilder arquivoSaida = new StringBuilder();
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    String pathArquivo = path.toString();
                    String hash = doHash(pathArquivo);
                    arquivoSaida.append("\n{CaminhoArquivo: ").append(pathArquivo).append(",\n")
                               .append("NomeArquivo: ").append(path.getFileName().toString()).append(",\n")
                               .append("Hash: ").append(hash).append("}");
                    System.out.println("Calculado o hash do arquivo: " + pathArquivo);
                }
            }
            System.out.println("Cálculo de hash concluído para todos os arquivos na pasta.");
            return arquivoSaida.toString();
        }
    }


    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        String folderPath = "C:\\Users\\Leandro Arraes\\Documents\\seguran\u00E7a info\\leandro\\teste"; // Insira o caminho para a pasta aqui
        String hashes = (new Hash()).doHashInFolder(folderPath);
        Path outputFile = Paths.get("listaDeHash.txt");
        Files.writeString(outputFile, hashes);
        System.out.println("As informações de hash foram gravadas no arquivo: " + outputFile);
    }
}
