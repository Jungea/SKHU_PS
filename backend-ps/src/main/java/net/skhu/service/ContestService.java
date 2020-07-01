package net.skhu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.skhu.domain.Contest;
import net.skhu.domain.Detail;
import net.skhu.domain.ProjectStar;
import net.skhu.model.ContestProjectModel;
import net.skhu.repository.ContestRepository;
import net.skhu.repository.DetailRepository;
import net.skhu.repository.ProjectStarRepository;

@Service
public class ContestService {
	@Autowired
	ContestRepository contestRepository;
	@Autowired
	DetailRepository detailRepository;
	@Autowired
	ProjectStarRepository projectStarRepository;
	
	public List<Detail> contestNames() {
		return detailRepository.findByCommon_CmnName("경진대회");
	}
	
	public Object[] contestYears() {
		return contestRepository.findDistictYearDESC();
	}

	public List<ContestProjectModel> contestProjects(int userId) {
		List<Contest> contests = contestRepository.findAllByOrderByYearDesc();
		List<ContestProjectModel> contestProjectModels = new ArrayList<>();
		
		for(Contest c : contests) {
			ContestProjectModel model = new ContestProjectModel();
			
			model.setContestId(c.getContestId());
			model.setDetail(c.getDetail());
			model.setProject(c.getProject());
			model.setYear(c.getYear());
			model.setPrize(c.getPrize());
			ProjectStar ps = projectStarRepository.findByUser_userIdAndProject_ProjectId(userId, model.getProject().getProjectId());
			model.setStar(ps != null ? true : false);
			
			contestProjectModels.add(model);
		}

		return contestProjectModels;
	}
	
	public Contest getModal(int contestId) {
		
		return contestRepository.findById(contestId).get();
		
		
//		Project p=projectRepository.findById(projectId).get();
//		ProjectBoardModel model = new ProjectBoardModel();
//		model.setProject(p);
//		ProjectStar ps = projectStarRepository.findByUser_userIdAndProject_ProjectId(userId, p.getProjectId());
//		model.setStar(ps != null ? true : false);
//		model.setSubjectName(p.getSubject() != null ? p.getSubject().getTitle() : null);
//		model.setCreateName(p.getUser().getName());
//		List<ProjectJoin> projectJoin = projectJoinRepository.findByProject_ProjectId(p.getProjectId());
//		Set<Integer> allGrade = new HashSet<>();
//		for (ProjectJoin pj : projectJoin) {
//			if(pj.getState()==1) {
//				allGrade.add(pj.getUser().getGrade());
//			}
//		}
//		model.setAllMemGrade(allGrade);
//		ProjectJoin pj = projectJoinRepository.findByUser_userIdAndProject_projectId(userId, p.getProjectId());
//		if (pj == null || pj.getState()==2) // 거절 상태이거나 신청 이력이 없을때
//			model.setState(2); // 프로젝트 신청할수 있는 상태
//		else if (pj.getState() == 0)
//			model.setState(0); // 승인대기 상태
//		else if (pj.getState() == 1)
//			model.setState(1); // 참가하고있는 상태
//		return model;
	}
	
}
