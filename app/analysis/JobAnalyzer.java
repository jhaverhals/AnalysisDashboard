package analysis;

import jsonhandling.BuildParser;
import jsonhandling.JobParser;
import jsonhandling.JsonReader;
import model.EntityHelper;
import model.jenkins.Job;

public class JobAnalyzer
{
	private final Job m_job;

	private final JobParser m_jobParser;

	private final JsonReader m_jsonReader;

	public JobAnalyzer(final Job job, final JobParser jobParser, final JsonReader jsonReader)
	{
		m_job = job;
		m_jobParser = jobParser;
		m_jsonReader = jsonReader;
	}

	public void analyze()
	{
		if (!m_jobParser.hasBuildInformationAvailable())
		{
			m_jobParser.loadBuildInformation(m_jsonReader);
		}

		m_job.setBuilding(m_jobParser.isBuilding());
		m_job.setStatus(m_jobParser.getStatus());
		m_job.setDescription(m_jobParser.getDescription());

		switch (m_jobParser.getStatus())
		{
		case NEW:
			return;
		case ABORTED:
		case DISABLED:
		case FAILED:
		case STABLE:
		case UNSTABLE:
			long lastCompletedBuildNumber = m_jobParser.getLastCompletedBuildNumber();
			if (!m_job.getLastBuildNumber().equals(Long.valueOf(lastCompletedBuildNumber)))
			{
				analyzeDetails();
				return;
			}
			break;
		default:
			break;
		}

		EntityHelper.persist(m_job);
	}

	private void analyzeDetails()
	{
		if (m_jobParser.isMatrixJob())
		{
			//TODO
		}
		else
		{
			BuildParser buildParser = m_jobParser.loadLastCompletedBuild(m_jsonReader);
			new RunAnalyzer(m_job, buildParser, m_jsonReader).analyze();

			//m_job.setLastBuildNumber(m_jobParser.getLastCompletedBuildNumber());
		}

	}
}
