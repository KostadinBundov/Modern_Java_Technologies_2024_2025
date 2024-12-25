package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID,
                          String accountID,
                          double transactionAmount,
                          LocalDateTime transactionDate,
                          String location,
                          Channel channel) {

    private static final int TRANSACTION_ID_INDEX = 0;
    private static final int ACCOUNT_ID_INDEX = 1;
    private static final int TRANSACTION_AMOUNT_INDEX = 2;
    private static final int TRANSACTION_DATE_INDEX = 3;
    private static final int LOCATION_INDEX = 4;
    private static final int CHANNEL_INDEX = 5;

    private static final int PROPERTIES_COUNT = 6;

    public static Transaction of(String line) {
        String[] args = line.split(",");

        if (args.length != PROPERTIES_COUNT) {
            throw new IllegalArgumentException("Mismatch in properties count!");
        }

        String transactionID = args[TRANSACTION_ID_INDEX];
        String accountID = args[ACCOUNT_ID_INDEX];
        double amount = Double.parseDouble(args[TRANSACTION_AMOUNT_INDEX]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(args[TRANSACTION_DATE_INDEX], formatter);

        String location = args[LOCATION_INDEX];
        Channel channel = Channel.valueOf(args[CHANNEL_INDEX].toUpperCase());

        return new Transaction(transactionID, accountID, amount, dateTime, location, channel);
    }
}
