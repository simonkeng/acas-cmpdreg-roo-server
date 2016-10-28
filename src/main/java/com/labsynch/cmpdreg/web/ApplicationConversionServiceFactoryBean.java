package com.labsynch.cmpdreg.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

import com.labsynch.cmpdreg.domain.CmpdRegAppSetting;
import com.labsynch.cmpdreg.domain.CompoundType;
import com.labsynch.cmpdreg.domain.CorpName;
import com.labsynch.cmpdreg.domain.FileList;
import com.labsynch.cmpdreg.domain.FileType;
import com.labsynch.cmpdreg.domain.Isotope;
import com.labsynch.cmpdreg.domain.LotAlias;
import com.labsynch.cmpdreg.domain.LotAliasKind;
import com.labsynch.cmpdreg.domain.LotAliasType;
import com.labsynch.cmpdreg.domain.Operator;
import com.labsynch.cmpdreg.domain.Parent;
import com.labsynch.cmpdreg.domain.ParentAlias;
import com.labsynch.cmpdreg.domain.ParentAliasKind;
import com.labsynch.cmpdreg.domain.ParentAliasType;
import com.labsynch.cmpdreg.domain.ParentAnnotation;
import com.labsynch.cmpdreg.domain.PhysicalState;
import com.labsynch.cmpdreg.domain.PreDef_CorpName;
import com.labsynch.cmpdreg.domain.Project;
import com.labsynch.cmpdreg.domain.PurityMeasuredBy;
import com.labsynch.cmpdreg.domain.Salt;
import com.labsynch.cmpdreg.domain.SaltForm;
import com.labsynch.cmpdreg.domain.Scientist;
import com.labsynch.cmpdreg.domain.SolutionUnit;
import com.labsynch.cmpdreg.domain.StereoCategory;
import com.labsynch.cmpdreg.domain.Unit;
import com.labsynch.cmpdreg.domain.Vendor;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<CmpdRegAppSetting, String> getCmpdRegAppSettingToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.CmpdRegAppSetting, java.lang.String>() {
            public String convert(CmpdRegAppSetting cmpdRegAppSetting) {
                return new StringBuilder().append(cmpdRegAppSetting.getPropName()).append(' ').append(cmpdRegAppSetting.getPropValue()).append(' ').append(cmpdRegAppSetting.getComments()).append(' ').append(cmpdRegAppSetting.getRecordedDate()).toString();
            }
        };
    }

	public Converter<Long, CmpdRegAppSetting> getIdToCmpdRegAppSettingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.CmpdRegAppSetting>() {
            public com.labsynch.cmpdreg.domain.CmpdRegAppSetting convert(java.lang.Long id) {
                return CmpdRegAppSetting.findCmpdRegAppSetting(id);
            }
        };
    }

	public Converter<String, CmpdRegAppSetting> getStringToCmpdRegAppSettingConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.CmpdRegAppSetting>() {
            public com.labsynch.cmpdreg.domain.CmpdRegAppSetting convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CmpdRegAppSetting.class);
            }
        };
    }

	public Converter<CorpName, String> getCorpNameToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.CorpName, java.lang.String>() {
            public String convert(CorpName corpName) {
                return new StringBuilder().append(corpName.getParentCorpName()).append(' ').append(corpName.getComment()).toString();
            }
        };
    }

	public Converter<Long, CorpName> getIdToCorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.CorpName>() {
            public com.labsynch.cmpdreg.domain.CorpName convert(java.lang.Long id) {
                return CorpName.findCorpName(id);
            }
        };
    }

	public Converter<String, CorpName> getStringToCorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.CorpName>() {
            public com.labsynch.cmpdreg.domain.CorpName convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CorpName.class);
            }
        };
    }

	public Converter<FileList, String> getFileListToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.FileList, java.lang.String>() {
            public String convert(FileList fileList) {
                return new StringBuilder().append(fileList.getFile()).append(' ').append(fileList.getDescription()).append(' ').append(fileList.getName()).append(' ').append(fileList.getType()).toString();
            }
        };
    }

	public Converter<Long, FileList> getIdToFileListConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.FileList>() {
            public com.labsynch.cmpdreg.domain.FileList convert(java.lang.Long id) {
                return FileList.findFileList(id);
            }
        };
    }

	public Converter<String, FileList> getStringToFileListConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.FileList>() {
            public com.labsynch.cmpdreg.domain.FileList convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FileList.class);
            }
        };
    }
	
	public Converter<ParentAliasType, String> getParentAliasTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.ParentAliasType, java.lang.String>() {
            public String convert(ParentAliasType parentAliasType) {
                return new StringBuilder().append(parentAliasType.getTypeName()).toString();
            }
        };
    }

	public Converter<FileType, String> getFileTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.FileType, java.lang.String>() {
            public String convert(FileType fileType) {
                return new StringBuilder().append(fileType.getName()).append(' ').append(fileType.getCode()).toString();
            }
        };
    }

	public Converter<Long, FileType> getIdToFileTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.FileType>() {
            public com.labsynch.cmpdreg.domain.FileType convert(java.lang.Long id) {
                return FileType.findFileType(id);
            }
        };
    }

	public Converter<String, FileType> getStringToFileTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.FileType>() {
            public com.labsynch.cmpdreg.domain.FileType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), FileType.class);
            }
        };
    }

	public Converter<Isotope, String> getIsotopeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Isotope, java.lang.String>() {
            public String convert(Isotope isotope) {
                return new StringBuilder().append(isotope.getName()).append(' ').append(isotope.getAbbrev()).append(' ').append(isotope.getMassChange()).toString();
            }
        };
    }

	public Converter<Long, Isotope> getIdToIsotopeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Isotope>() {
            public com.labsynch.cmpdreg.domain.Isotope convert(java.lang.Long id) {
                return Isotope.findIsotope(id);
            }
        };
    }

	public Converter<String, Isotope> getStringToIsotopeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Isotope>() {
            public com.labsynch.cmpdreg.domain.Isotope convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Isotope.class);
            }
        };
    }

	public Converter<Operator, String> getOperatorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Operator, java.lang.String>() {
            public String convert(Operator operator) {
                return new StringBuilder().append(operator.getName()).append(' ').append(operator.getCode()).toString();
            }
        };
    }

	public Converter<Long, Operator> getIdToOperatorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Operator>() {
            public com.labsynch.cmpdreg.domain.Operator convert(java.lang.Long id) {
                return Operator.findOperator(id);
            }
        };
    }

	public Converter<String, Operator> getStringToOperatorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Operator>() {
            public com.labsynch.cmpdreg.domain.Operator convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Operator.class);
            }
        };
    }

	public Converter<Parent, String> getParentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Parent, java.lang.String>() {
            public String convert(Parent parent) {
                return new StringBuilder().append(parent.getId()).toString();
            }
        };
    }

	public Converter<Long, Parent> getIdToParentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Parent>() {
            public com.labsynch.cmpdreg.domain.Parent convert(java.lang.Long id) {
                return Parent.findParent(id);
            }
        };
    }

	public Converter<String, Parent> getStringToParentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Parent>() {
            public com.labsynch.cmpdreg.domain.Parent convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Parent.class);
            }
        };
    }

	public Converter<PhysicalState, String> getPhysicalStateToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.PhysicalState, java.lang.String>() {
            public String convert(PhysicalState physicalState) {
                return new StringBuilder().append(physicalState.getName()).append(' ').append(physicalState.getCode()).toString();
            }
        };
    }

	public Converter<Long, PhysicalState> getIdToPhysicalStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.PhysicalState>() {
            public com.labsynch.cmpdreg.domain.PhysicalState convert(java.lang.Long id) {
                return PhysicalState.findPhysicalState(id);
            }
        };
    }

	public Converter<String, PhysicalState> getStringToPhysicalStateConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.PhysicalState>() {
            public com.labsynch.cmpdreg.domain.PhysicalState convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PhysicalState.class);
            }
        };
    }

	public Converter<PreDef_CorpName, String> getPreDef_CorpNameToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.PreDef_CorpName, java.lang.String>() {
            public String convert(PreDef_CorpName preDef_CorpName) {
                return new StringBuilder().append(preDef_CorpName.getCorpNumber()).append(' ').append(preDef_CorpName.getCorpName()).append(' ').append(preDef_CorpName.getComment()).toString();
            }
        };
    }

	public Converter<Long, PreDef_CorpName> getIdToPreDef_CorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.PreDef_CorpName>() {
            public com.labsynch.cmpdreg.domain.PreDef_CorpName convert(java.lang.Long id) {
                return PreDef_CorpName.findPreDef_CorpName(id);
            }
        };
    }

	public Converter<String, PreDef_CorpName> getStringToPreDef_CorpNameConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.PreDef_CorpName>() {
            public com.labsynch.cmpdreg.domain.PreDef_CorpName convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PreDef_CorpName.class);
            }
        };
    }

	public Converter<Project, String> getProjectToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Project, java.lang.String>() {
            public String convert(Project project) {
                return new StringBuilder().append(project.getName()).append(' ').append(project.getCode()).toString();
            }
        };
    }

	public Converter<Long, Project> getIdToProjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Project>() {
            public com.labsynch.cmpdreg.domain.Project convert(java.lang.Long id) {
                return Project.findProject(id);
            }
        };
    }

	public Converter<String, Project> getStringToProjectConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Project>() {
            public com.labsynch.cmpdreg.domain.Project convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Project.class);
            }
        };
    }

	public Converter<PurityMeasuredBy, String> getPurityMeasuredByToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.PurityMeasuredBy, java.lang.String>() {
            public String convert(PurityMeasuredBy purityMeasuredBy) {
                return new StringBuilder().append(purityMeasuredBy.getName()).append(' ').append(purityMeasuredBy.getCode()).toString();
            }
        };
    }

	public Converter<Long, PurityMeasuredBy> getIdToPurityMeasuredByConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.PurityMeasuredBy>() {
            public com.labsynch.cmpdreg.domain.PurityMeasuredBy convert(java.lang.Long id) {
                return PurityMeasuredBy.findPurityMeasuredBy(id);
            }
        };
    }

	public Converter<String, PurityMeasuredBy> getStringToPurityMeasuredByConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.PurityMeasuredBy>() {
            public com.labsynch.cmpdreg.domain.PurityMeasuredBy convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), PurityMeasuredBy.class);
            }
        };
    }

	public Converter<Salt, String> getSaltToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Salt, java.lang.String>() {
            public String convert(Salt salt) {
                return new StringBuilder().append(salt.getMolStructure()).append(' ').append(salt.getName()).append(' ').append(salt.getOriginalStructure()).append(' ').append(salt.getAbbrev()).toString();
            }
        };
    }

	public Converter<Long, Salt> getIdToSaltConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Salt>() {
            public com.labsynch.cmpdreg.domain.Salt convert(java.lang.Long id) {
                return Salt.findSalt(id);
            }
        };
    }

	public Converter<String, Salt> getStringToSaltConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Salt>() {
            public com.labsynch.cmpdreg.domain.Salt convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Salt.class);
            }
        };
    }

	public Converter<SaltForm, String> getSaltFormToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.SaltForm, java.lang.String>() {
            public String convert(SaltForm saltForm) {
                return new StringBuilder().append(saltForm.getId()).toString();
            }
        };
    }

	public Converter<Long, SaltForm> getIdToSaltFormConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.SaltForm>() {
            public com.labsynch.cmpdreg.domain.SaltForm convert(java.lang.Long id) {
                return SaltForm.findSaltForm(id);
            }
        };
    }

	public Converter<String, SaltForm> getStringToSaltFormConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.SaltForm>() {
            public com.labsynch.cmpdreg.domain.SaltForm convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SaltForm.class);
            }
        };
    }

	public Converter<Scientist, String> getScientistToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Scientist, java.lang.String>() {
            public String convert(Scientist scientist) {
                return new StringBuilder().append(scientist.getCode()).append(' ').append(scientist.getName()).toString();
            }
        };
    }

	public Converter<Long, Scientist> getIdToScientistConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Scientist>() {
            public com.labsynch.cmpdreg.domain.Scientist convert(java.lang.Long id) {
                return Scientist.findScientist(id);
            }
        };
    }

	public Converter<String, Scientist> getStringToScientistConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Scientist>() {
            public com.labsynch.cmpdreg.domain.Scientist convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Scientist.class);
            }
        };
    }

	public Converter<SolutionUnit, String> getSolutionUnitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.SolutionUnit, java.lang.String>() {
            public String convert(SolutionUnit solutionUnit) {
                return new StringBuilder().append(solutionUnit.getName()).append(' ').append(solutionUnit.getCode()).toString();
            }
        };
    }

	public Converter<Long, SolutionUnit> getIdToSolutionUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.SolutionUnit>() {
            public com.labsynch.cmpdreg.domain.SolutionUnit convert(java.lang.Long id) {
                return SolutionUnit.findSolutionUnit(id);
            }
        };
    }

	public Converter<String, SolutionUnit> getStringToSolutionUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.SolutionUnit>() {
            public com.labsynch.cmpdreg.domain.SolutionUnit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), SolutionUnit.class);
            }
        };
    }

	public Converter<StereoCategory, String> getStereoCategoryToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.StereoCategory, java.lang.String>() {
            public String convert(StereoCategory stereoCategory) {
                return new StringBuilder().append(stereoCategory.getName()).append(' ').append(stereoCategory.getCode()).toString();
            }
        };
    }

	public Converter<Long, StereoCategory> getIdToStereoCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.StereoCategory>() {
            public com.labsynch.cmpdreg.domain.StereoCategory convert(java.lang.Long id) {
                return StereoCategory.findStereoCategory(id);
            }
        };
    }

	public Converter<String, StereoCategory> getStringToStereoCategoryConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.StereoCategory>() {
            public com.labsynch.cmpdreg.domain.StereoCategory convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), StereoCategory.class);
            }
        };
    }

	public Converter<Unit, String> getUnitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Unit, java.lang.String>() {
            public String convert(Unit unit) {
                return new StringBuilder().append(unit.getName()).append(' ').append(unit.getCode()).toString();
            }
        };
    }

	public Converter<Long, Unit> getIdToUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Unit>() {
            public com.labsynch.cmpdreg.domain.Unit convert(java.lang.Long id) {
                return Unit.findUnit(id);
            }
        };
    }

	public Converter<String, Unit> getStringToUnitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Unit>() {
            public com.labsynch.cmpdreg.domain.Unit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Unit.class);
            }
        };
    }

	public Converter<Vendor, String> getVendorToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.Vendor, java.lang.String>() {
            public String convert(Vendor vendor) {
                return new StringBuilder().append(vendor.getName()).append(' ').append(vendor.getCode()).toString();
            }
        };
    }

	public Converter<Long, Vendor> getIdToVendorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.Vendor>() {
            public com.labsynch.cmpdreg.domain.Vendor convert(java.lang.Long id) {
                return Vendor.findVendor(id);
            }
        };
    }

	public Converter<String, Vendor> getStringToVendorConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.Vendor>() {
            public com.labsynch.cmpdreg.domain.Vendor convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Vendor.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getCmpdRegAppSettingToStringConverter());
        registry.addConverter(getIdToCmpdRegAppSettingConverter());
        registry.addConverter(getStringToCmpdRegAppSettingConverter());
        registry.addConverter(getCorpNameToStringConverter());
        registry.addConverter(getIdToCorpNameConverter());
        registry.addConverter(getStringToCorpNameConverter());
        registry.addConverter(getFileListToStringConverter());
        registry.addConverter(getIdToFileListConverter());
        registry.addConverter(getStringToFileListConverter());
        registry.addConverter(getFileTypeToStringConverter());
        registry.addConverter(getIdToFileTypeConverter());
        registry.addConverter(getStringToFileTypeConverter());
        registry.addConverter(getIsotopeToStringConverter());
        registry.addConverter(getIdToIsotopeConverter());
        registry.addConverter(getStringToIsotopeConverter());
        registry.addConverter(getOperatorToStringConverter());
        registry.addConverter(getIdToOperatorConverter());
        registry.addConverter(getStringToOperatorConverter());
        registry.addConverter(getParentToStringConverter());
        registry.addConverter(getIdToParentConverter());
        registry.addConverter(getStringToParentConverter());
        registry.addConverter(getPhysicalStateToStringConverter());
        registry.addConverter(getIdToPhysicalStateConverter());
        registry.addConverter(getStringToPhysicalStateConverter());
        registry.addConverter(getPreDef_CorpNameToStringConverter());
        registry.addConverter(getIdToPreDef_CorpNameConverter());
        registry.addConverter(getStringToPreDef_CorpNameConverter());
        registry.addConverter(getProjectToStringConverter());
        registry.addConverter(getIdToProjectConverter());
        registry.addConverter(getStringToProjectConverter());
        registry.addConverter(getPurityMeasuredByToStringConverter());
        registry.addConverter(getIdToPurityMeasuredByConverter());
        registry.addConverter(getStringToPurityMeasuredByConverter());
        registry.addConverter(getSaltToStringConverter());
        registry.addConverter(getIdToSaltConverter());
        registry.addConverter(getStringToSaltConverter());
        registry.addConverter(getSaltFormToStringConverter());
        registry.addConverter(getIdToSaltFormConverter());
        registry.addConverter(getStringToSaltFormConverter());
        registry.addConverter(getScientistToStringConverter());
        registry.addConverter(getIdToScientistConverter());
        registry.addConverter(getStringToScientistConverter());
        registry.addConverter(getSolutionUnitToStringConverter());
        registry.addConverter(getIdToSolutionUnitConverter());
        registry.addConverter(getStringToSolutionUnitConverter());
        registry.addConverter(getStereoCategoryToStringConverter());
        registry.addConverter(getIdToStereoCategoryConverter());
        registry.addConverter(getStringToStereoCategoryConverter());
        registry.addConverter(getUnitToStringConverter());
        registry.addConverter(getIdToUnitConverter());
        registry.addConverter(getStringToUnitConverter());
        registry.addConverter(getVendorToStringConverter());
        registry.addConverter(getIdToVendorConverter());
        registry.addConverter(getStringToVendorConverter());
        registry.addConverter(getParentAliasTypeToStringConverter());
        
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }

	public Converter<CompoundType, String> getCompoundTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.CompoundType, java.lang.String>() {
            public String convert(CompoundType compoundType) {
                return new StringBuilder().append(compoundType.getCode()).toString();
            }
        };
    }

	public Converter<Long, CompoundType> getIdToCompoundTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.CompoundType>() {
            public com.labsynch.cmpdreg.domain.CompoundType convert(java.lang.Long id) {
                return CompoundType.findCompoundType(id);
            }
        };
    }

	public Converter<String, CompoundType> getStringToCompoundTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.CompoundType>() {
            public com.labsynch.cmpdreg.domain.CompoundType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), CompoundType.class);
            }
        };
    }

	public Converter<LotAlias, String> getLotAliasToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.LotAlias, java.lang.String>() {
            public String convert(LotAlias lotAlias) {
                return new StringBuilder().append(lotAlias.getLsType()).append(' ').append(lotAlias.getLsKind()).append(' ').append(lotAlias.getAliasName()).toString();
            }
        };
    }

	public Converter<Long, LotAlias> getIdToLotAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.LotAlias>() {
            public com.labsynch.cmpdreg.domain.LotAlias convert(java.lang.Long id) {
                return LotAlias.findLotAlias(id);
            }
        };
    }

	public Converter<String, LotAlias> getStringToLotAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.LotAlias>() {
            public com.labsynch.cmpdreg.domain.LotAlias convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LotAlias.class);
            }
        };
    }

	public Converter<LotAliasKind, String> getLotAliasKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.LotAliasKind, java.lang.String>() {
            public String convert(LotAliasKind lotAliasKind) {
                return new StringBuilder().append(lotAliasKind.getKindName()).toString();
            }
        };
    }

	public Converter<Long, LotAliasKind> getIdToLotAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.LotAliasKind>() {
            public com.labsynch.cmpdreg.domain.LotAliasKind convert(java.lang.Long id) {
                return LotAliasKind.findLotAliasKind(id);
            }
        };
    }

	public Converter<String, LotAliasKind> getStringToLotAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.LotAliasKind>() {
            public com.labsynch.cmpdreg.domain.LotAliasKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LotAliasKind.class);
            }
        };
    }

	public Converter<LotAliasType, String> getLotAliasTypeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.LotAliasType, java.lang.String>() {
            public String convert(LotAliasType lotAliasType) {
                return new StringBuilder().append(lotAliasType.getTypeName()).toString();
            }
        };
    }

	public Converter<Long, LotAliasType> getIdToLotAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.LotAliasType>() {
            public com.labsynch.cmpdreg.domain.LotAliasType convert(java.lang.Long id) {
                return LotAliasType.findLotAliasType(id);
            }
        };
    }

	public Converter<String, LotAliasType> getStringToLotAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.LotAliasType>() {
            public com.labsynch.cmpdreg.domain.LotAliasType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), LotAliasType.class);
            }
        };
    }

	public Converter<ParentAlias, String> getParentAliasToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.ParentAlias, java.lang.String>() {
            public String convert(ParentAlias parentAlias) {
                return new StringBuilder().append(parentAlias.getLsType()).append(' ').append(parentAlias.getLsKind()).append(' ').append(parentAlias.getAliasName()).append(' ').append(parentAlias.getSortId()).toString();
            }
        };
    }

	public Converter<Long, ParentAlias> getIdToParentAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.ParentAlias>() {
            public com.labsynch.cmpdreg.domain.ParentAlias convert(java.lang.Long id) {
                return ParentAlias.findParentAlias(id);
            }
        };
    }

	public Converter<String, ParentAlias> getStringToParentAliasConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.ParentAlias>() {
            public com.labsynch.cmpdreg.domain.ParentAlias convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAlias.class);
            }
        };
    }

	public Converter<ParentAliasKind, String> getParentAliasKindToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.ParentAliasKind, java.lang.String>() {
            public String convert(ParentAliasKind parentAliasKind) {
                return new StringBuilder().append(parentAliasKind.getKindName()).toString();
            }
        };
    }

	public Converter<Long, ParentAliasKind> getIdToParentAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.ParentAliasKind>() {
            public com.labsynch.cmpdreg.domain.ParentAliasKind convert(java.lang.Long id) {
                return ParentAliasKind.findParentAliasKind(id);
            }
        };
    }

	public Converter<String, ParentAliasKind> getStringToParentAliasKindConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.ParentAliasKind>() {
            public com.labsynch.cmpdreg.domain.ParentAliasKind convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAliasKind.class);
            }
        };
    }

	public Converter<Long, ParentAliasType> getIdToParentAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.ParentAliasType>() {
            public com.labsynch.cmpdreg.domain.ParentAliasType convert(java.lang.Long id) {
                return ParentAliasType.findParentAliasType(id);
            }
        };
    }

	public Converter<String, ParentAliasType> getStringToParentAliasTypeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.ParentAliasType>() {
            public com.labsynch.cmpdreg.domain.ParentAliasType convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAliasType.class);
            }
        };
    }

	public Converter<ParentAnnotation, String> getParentAnnotationToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.labsynch.cmpdreg.domain.ParentAnnotation, java.lang.String>() {
            public String convert(ParentAnnotation parentAnnotation) {
                return new StringBuilder().append(parentAnnotation.getCode()).append(' ').append(parentAnnotation.getName()).append(' ').append(parentAnnotation.getDisplayOrder()).append(' ').append(parentAnnotation.getComment()).toString();
            }
        };
    }

	public Converter<Long, ParentAnnotation> getIdToParentAnnotationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.labsynch.cmpdreg.domain.ParentAnnotation>() {
            public com.labsynch.cmpdreg.domain.ParentAnnotation convert(java.lang.Long id) {
                return ParentAnnotation.findParentAnnotation(id);
            }
        };
    }

	public Converter<String, ParentAnnotation> getStringToParentAnnotationConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.labsynch.cmpdreg.domain.ParentAnnotation>() {
            public com.labsynch.cmpdreg.domain.ParentAnnotation convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), ParentAnnotation.class);
            }
        };
    }
}
