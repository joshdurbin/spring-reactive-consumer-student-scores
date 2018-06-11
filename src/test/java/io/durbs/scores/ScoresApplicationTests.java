package io.durbs.scores;

import io.durbs.scores.domain.ScoreSummary;
import io.durbs.scores.domain.Score;
import io.durbs.scores.repo.ScoreRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScoresApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private ScoreRepository scoreRepository;

	@Before
	public void after() {
		scoreRepository.purge();
	}

	@Test
	public void singleExamSinglePersonCalculation() {

		val josh = new Score("josh.durbin", 93029, .9483703);
		val joshSummary = ScoreSummary.builder().scores(Sets.newSet(josh)).average(josh.getScore()).build();

		scoreRepository.saveScore(josh);

		val students = testRestTemplate.exchange(
				"/students",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<String>>() {}).getBody();

		val exams = testRestTemplate.exchange(
				"/exams",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Integer>>() {}).getBody();

		assertEquals(students.size(), 1);
		assertEquals(students.get(0), josh.getStudentId());
		assertEquals(exams.size(), 1);
		assertEquals(exams.get(0), josh.getExam());
		assertEquals(testRestTemplate.getForObject("/students/" + josh.getStudentId(), ScoreSummary.class), joshSummary);
		assertEquals(testRestTemplate.getForObject("/exams/" + josh.getExam(), ScoreSummary.class), joshSummary);
	}

	@Test
	public void testMissingExam() {

		assertNull(testRestTemplate.getForObject("/students/asdf", ScoreSummary.class));
	}

	@Test
	public void testMissingStudent() {

		assertNull(testRestTemplate.getForObject("/exams/1", ScoreSummary.class));
	}

	@Test
	public void twoExamThreePersonCalculation() {

		val josh = new Score("josh.durbin", 1, .873923);
		val andrea = new Score("andrea.lietz", 1, .993910);
		val deedee = new Score("deedee.durbin", 2, .920393);

		val joshSummary = new ScoreSummary(Sets.newSet(josh), josh.getScore());

		scoreRepository.saveScore(josh);
		scoreRepository.saveScore(andrea);
		scoreRepository.saveScore(deedee);

		val students = testRestTemplate.exchange(
				"/students",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<String>>() {}).getBody();

		val exams = testRestTemplate.exchange(
				"/exams",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Integer>>() {}).getBody();

		assertEquals(students.size(), 3);
		assertEquals(exams.size(), 2);
		assertEquals(testRestTemplate.getForObject("/students/" + josh.getStudentId(), ScoreSummary.class),
				ScoreSummary.builder().scores(Sets.newSet(josh)).average(josh.getScore()).build());
		assertEquals(testRestTemplate.getForObject("/exams/1", ScoreSummary.class),
				ScoreSummary.builder().scores(Sets.newSet(josh, andrea)).average((josh.getScore() + andrea.getScore()) / 2).build());
		assertEquals(testRestTemplate.getForObject("/exams/2", ScoreSummary.class),
				ScoreSummary.builder().scores(Sets.newSet(deedee)).average(deedee.getScore()).build());
	}

}
