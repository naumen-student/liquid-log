package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;

public class ActionDoneParserTest {
    private DataSet ds = new DataSet();

    @Test
    public void mustParseAddAction() {
        //given
        ActionDoneParser parser = new ActionDoneParser();

        //when
        parser.parseLine("Done(10): AddObjectAction", ds);

        //then
        Assert.assertEquals(1, parser.getAddObjectActions());
    }

    @Test
    public void mustParseFormActions() {
        //given
        ActionDoneParser parser = new ActionDoneParser();

        //when
        parser.parseLine("Done(10): GetFormAction", ds);
        parser.parseLine("Done(1): GetAddFormContextDataAction", ds);

        //then
        Assert.assertEquals(2, parser.getFormActions());
    }

    @Test
    public void mustParseEditObject() {
        //given
        ActionDoneParser parser=  new ActionDoneParser();

        //when
        parser.parseLine("Done(10): EditObjectAction", ds);

        //then
        Assert.assertEquals(1, parser.getEditObjectsActions());
    }

    @Test
    public void mustParseSearchObject(){
        //given
        ActionDoneParser parser = new ActionDoneParser();

        //when
        parser.parseLine("Done(10): GetPossibleAgreementsChildsSearchAction", ds);
        parser.parseLine("Done(10): TreeSearchAction", ds);
        parser.parseLine("Done(10): GetSearchResultAction", ds);
        parser.parseLine("Done(10): GetSimpleSearchResultsAction", ds);
        parser.parseLine("Done(10): SimpleSearchAction", ds);
        parser.parseLine("Done(10): ExtendedSearchByStringAction", ds);
        parser.parseLine("Done(10): ExtendedSearchByFilterAction", ds);

        //then
        Assert.assertEquals(7, parser.getSearchActions());
    }

    @Test
    public void mustParseGetList(){
        //given:
        ActionDoneParser parser=  new ActionDoneParser();

        //when:
        parser.parseLine("Done(10): GetDtObjectListAction", ds);
        parser.parseLine("Done(10): GetPossibleCaseListValueAction", ds);
        parser.parseLine("Done(10): GetPossibleAgreementsTreeListActions", ds);
        parser.parseLine("Done(10): GetCountForObjectListAction", ds);
        parser.parseLine("Done(10): GetDataForObjectListAction", ds);
        parser.parseLine("Done(10): GetPossibleAgreementsListActions", ds);
        parser.parseLine("Done(10): GetDtObjectForRelObjListAction", ds);

        //then:
        Assert.assertEquals(7, parser.geListActions());
    }

    @Test
    public void mustParseComment(){
        //given:
        ActionDoneParser parser=  new ActionDoneParser();

        //when:
        parser.parseLine("Done(10): EditCommentAction", ds);
        parser.parseLine("Done(10): ChangeResponsibleWithAddCommentAction", ds);
        parser.parseLine("Done(10): ShowMoreCommentAttrsAction", ds);
        parser.parseLine("Done(10): CheckObjectsExceedsCommentsAmountAction", ds);
        parser.parseLine("Done(10): GetAddCommentPermissionAction", ds);
        parser.parseLine("Done(10): GetCommentDtObjectTemplateAction", ds);

        //then:
        Assert.assertEquals(6, parser.getCommentActions());
    }

    @Test
    public void mustParseDtObject(){
        //given:
        ActionDoneParser parser=  new ActionDoneParser();

        //when:
        parser.parseLine("Done(10): GetVisibleDtObjectAction", ds);
        parser.parseLine("Done(10): GetDtObjectsAction", ds);
        parser.parseLine("Done(10): GetDtObjectTreeSelectionStateAction", ds);
        parser.parseLine("Done(10): AbstractGetDtObjectTemplateAction", ds);
        parser.parseLine("Done(10): GetDtObjectTemplateAction", ds);

        //then:
        Assert.assertEquals(5, parser.getDtObjectActions());
    }

}
