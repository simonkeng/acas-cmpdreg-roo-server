package com.labsynch.cmpdreg.domain;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.TypedQuery;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = {"findStandardizationDryrunCompoundsByCorpNameEquals","findStandardizationDryrunCompoundsByCdId" })
public class StandardizationDryrunCompound {

	// id, runNumber, qcDate, parentId, corpName, dupeCount, dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment
	private int runNumber;

	private Date qcDate;

	private Long parentId;

	private String corpName;

	private String newDuplicates;
	
	private String oldDuplicates;
	
	private boolean changedStructure;
	
	private Double oldMolWeight;
	
	private Double newMolWeight;
	
	private boolean displayChange;

	private boolean asDrawnDisplayChange;

	private int newDupeCount;

	private int oldDupeCount;

	private String alias;

	private String stereoCategory;

	private String stereoComment;

	private int CdId;

	@Column(columnDefinition = "text")
	private String molStructure;

	private String comment;

	private boolean ignore;

	public StandardizationDryrunCompound() {
	}

	public static List<Long> getAllIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public static TypedQuery<Long> findAllIds() {
		return StandardizationDryrunCompound.entityManager().createQuery("SELECT o.id FROM StandardizationDryrunCompound o", Long.class);
	}

	@Transactional
	public static void truncateTable() {
		int output = StandardizationDryrunCompound.entityManager().createNativeQuery("TRUNCATE standardization_dryrun_compound").executeUpdate();
	}

	public static TypedQuery<Integer> findMaxRunNumber() {
		return StandardizationDryrunCompound.entityManager().createQuery("SELECT max(o.runNumber) FROM StandardizationDryrunCompound o", Integer.class);
	}

	public static TypedQuery<StandardizationDryrunCompound> findStandardizationChanges() {
		String querySQL = "SELECT o FROM StandardizationDryrunCompound o WHERE changedStructure = true OR oldDupeCount > 0 OR newDupeCount > 0 OR displayChange = true";
		return StandardizationDryrunCompound.entityManager().createQuery(querySQL, StandardizationDryrunCompound.class);
	}
	
	public static TypedQuery<Long> findParentIdsWithStandardizationChanges() {
		String querySQL = "SELECT o.parentId FROM StandardizationDryrunCompound o WHERE changedStructure = true OR oldDupeCount > 0 OR newDupeCount > 0 OR displayChange = true";
		return StandardizationDryrunCompound.entityManager().createQuery(querySQL, Long.class);
	}
	
	public static TypedQuery<Long> findParentsWithDisplayChanges() {
		String querySQL = "SELECT o.parentId FROM StandardizationDryrunCompound o WHERE displayChange = true";
		return StandardizationDryrunCompound.entityManager().createQuery(querySQL, Long.class);
	}
	
	public static StandardizationHistory fetchStats() {
//		String querySQL = "SELECT o.parentId FROM StandardizationDryrunCompound o WHERE displayChange = true";
//		Query q = StandardizationDryrunCompound.entityManager().createNativeQuery(querySQL);
		StandardizationHistory stats = new StandardizationHistory();
		stats.setSettings(StandardizationDryrunCompound.entityManager().createQuery("SELECT s.currentSettings FROM StandardizationSettings s", String.class).getSingleResult());
		stats.setSettingsHash(StandardizationDryrunCompound.entityManager().createQuery("SELECT s.currentSettingsHash FROM StandardizationSettings s", Integer.class).getSingleResult());
		stats.setChangedStructureCount(StandardizationDryrunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryrunCompound s WHERE s.changedStructure = true", Long.class).getSingleResult());
		stats.setOldDuplicateCount(StandardizationDryrunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryrunCompound s WHERE s.oldDupeCount > 0", Long.class).getSingleResult());
		stats.setNewDuplicateCount(StandardizationDryrunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryrunCompound s WHERE s.newDupeCount > 0", Long.class).getSingleResult());
		stats.setDisplayChangeCount(StandardizationDryrunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryrunCompound s WHERE s.displayChange = true", Long.class).getSingleResult());
		stats.setAsDrawnDisplayChangeCount(StandardizationDryrunCompound.entityManager().createQuery("SELECT count(s.id) FROM StandardizationDryrunCompound s WHERE s.asDrawnDisplayChange = true", Long.class).getSingleResult());

		return(stats);
	}
}
