package app.util;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.reactivestreams.Publisher;

import app.entity.hibernate.identifier.IdentifiableEntity;
import app.entity.hibernate.identifier.generated.AutoTypeEntity;
import app.entity.hibernate.identifier.generated.IdentityTypeEntity;
import app.entity.hibernate.identifier.generated.SequenceTypeEntity;
import app.entity.hibernate.identifier.generated.UUIDv4TypeEntity;
import jakarta.persistence.GenerationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageGenerator {
	private static final Random RANDOM = new Random();
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int MESSAGE_LENGTH = 100; // Length of each message

	public static String generateRandomString() {
		StringBuilder sb = new StringBuilder(MESSAGE_LENGTH);
		for (int i = 0; i < MESSAGE_LENGTH; i++) {
			int index = RANDOM.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}
		return sb.toString();
	}

	public static IdentifiableEntity generateEntityByType(GenerationType type) {
		log.debug("Message generated");
		return switch (type) {
			case IDENTITY -> new IdentityTypeEntity(generateRandomString());
			case AUTO -> new AutoTypeEntity(generateRandomString());
			case SEQUENCE -> new SequenceTypeEntity(generateRandomString());
			case UUID -> new UUIDv4TypeEntity(generateRandomString());
			default -> null;
		};
	}



	public static List<IdentifiableEntity> generateMessagesList(int size, GenerationType type) {
		return IntStream.range(1, size + 1)
				.mapToObj(integer -> MessageGenerator.generateEntityByType(type)).toList();
	}

}
