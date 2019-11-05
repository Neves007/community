package life.majiang.community.service;

import life.majiang.community.advice.CustomizeExceptionHandler;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.QuestionExtMapper;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import life.majiang.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class QuestionService {
    //主要用于整合questionDTO，用于合并quesitonMapper和UserMapper

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
        @Autowired
        private QuestionExtMapper questionExtMapper;

    //返回页面所需信息
    public PaginationDTO list(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        Integer totalPage;
        if (totalCount % size==0){
            totalPage = totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }
        paginationDTO.setTotalPage(totalPage);
        //设置pagination的分页信息

        if(page<1){
            page=1;
        }
        if(page>paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }
        paginationDTO.setPagination(page,size);
        Integer offset = size*(page-1);
        Integer limit = size;
        //获取问题
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, limit));
        //返回questionDTO列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        //获取问题对应的user信息，并整合questionDTO，把整合好的questionDTO加入questionDTOList
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);  //把quesition对象的所有属性拷贝到quesitionDTO上
            questionDTO.setUser(user); //设置questionDTO用户信息
            questionDTOList.add(questionDTO);
        }
        //设置paginatio的问题信息
        paginationDTO.setQuestions(questionDTOList);


        return paginationDTO;
    }

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        Integer totalPage; //问题页数
        if (totalCount % size==0){
            totalPage = totalCount/size;
        }else {
            totalPage=totalCount/size+1;
        }
        paginationDTO.setTotalPage(totalPage);
        //设置pagination的分页信息

        if(page<1){
            page=1;
        }
        if(page>paginationDTO.getTotalPage()){
            page=paginationDTO.getTotalPage();
        }
        paginationDTO.setPagination(page,size);
        Integer offset = size*(page-1);
        Integer limit = size;
        //获取问题
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example, new RowBounds(offset, limit));
        //返回questionDTO列表
        List<QuestionDTO> questionDTOList = new ArrayList<>();


        //获取问题对应的user信息，并整合questionDTO，把整合好的questionDTO加入questionDTOList
        for (Question question : questions) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);  //把quesition对象的所有属性拷贝到quesitionDTO上
            questionDTO.setUser(user); //设置questionDTO用户信息
            questionDTOList.add(questionDTO);
        }
        //设置paginatio的问题信息
        paginationDTO.setQuestions(questionDTOList);


        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOTE_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user); //设置questionDTO用户信息
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId()== null){
            //创建
            try {
                questionMapper.insertSelective(question);
            }catch (Exception e){
                System.out.println(e);
            }

        }else {
            //更新
           QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andCreatorEqualTo(question.getId());

            int updated = questionMapper.updateByExampleSelective(question,example);
            if (updated !=1 ){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOTE_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }
}
