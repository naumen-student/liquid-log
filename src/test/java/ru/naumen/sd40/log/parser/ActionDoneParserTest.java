package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;

public class ActionDoneParserTest {

    @Test
    public void mustParseAddAction() {
        //given
        ActionDoneParser parser = new ActionDoneParser();

        //when
        parser.parseLine("Done(10): AddObjectAction");

        //then
        Assert.assertEquals(1, parser.getAddObjectActions());
    }

    @Test
    public void mustParseFormActions() {
        //given
        ActionDoneParser parser = new ActionDoneParser();

        //when
        parser.parseLine("Done(10): GetFormAction");
        parser.parseLine("Done(1): GetAddFormContextDataAction");

        //then
        Assert.assertEquals(2, parser.getFormActions());
    }

    @Test
    public void mustParseEditObject() {
        //given
        ActionDoneParser parser=  new ActionDoneParser();

        //when
        parser.parseLine("Done(10): EditObjectAction");

        //then
        Assert.assertEquals(1, parser.getEditObjectsActions());
    }

    @Test
    public void mustParseSearchObject(){
        //given
        ActionDoneParser parser = new ActionDoneParser();

        //when
        parser.parseLine("Done(10): GetPossibleAgreementsChildsSearchAction");
        parser.parseLine("Done(10): TreeSearchAction");
        parser.parseLine("Done(10): GetSearchResultAction");
        parser.parseLine("Done(10): GetSimpleSearchResultsAction");
        parser.parseLine("Done(10): SimpleSearchAction");
        parser.parseLine("Done(10): ExtendedSearchByStringAction");
        parser.parseLine("Done(10): ExtendedSearchByFilterAction");

        //then
        Assert.assertEquals(7, parser.getSearchActions());
    }

    @Test
    public void mustParseGetList(){
        //given:
        ActionDoneParser parser=  new ActionDoneParser();

        //when:
        parser.parseLine("Done(10): GetDtObjectListAction");
        parser.parseLine("Done(10): GetPossibleCaseListValueAction");
        parser.parseLine("Done(10): GetPossibleAgreementsTreeListActions");
        parser.parseLine("Done(10): GetCountForObjectListAction");
        parser.parseLine("Done(10): GetDataForObjectListAction");
        parser.parseLine("Done(10): GetPossibleAgreementsListActions");
        parser.parseLine("Done(10): GetDtObjectForRelObjListAction");

        //then:
        Assert.assertEquals(7, parser.getListActions());
    }

    @Test
    public void mustParseComment(){
        //given:
        ActionDoneParser parser=  new ActionDoneParser();

        //when:
        parser.parseLine("Done(10): EditCommentAction");
        parser.parseLine("Done(10): ChangeResponsibleWithAddCommentAction");
        parser.parseLine("Done(10): ShowMoreCommentAttrsAction");
        parser.parseLine("Done(10): CheckObjectsExceedsCommentsAmountAction");
        parser.parseLine("Done(10): GetAddCommentPermissionAction");
        parser.parseLine("Done(10): GetCommentDtObjectTemplateAction");

        //then:
        Assert.assertEquals(6, parser.getCommentActions());
    }

    @Test
    public void mustParseDtObject(){
        //given:
        ActionDoneParser parser=  new ActionDoneParser();

        //when:
        parser.parseLine("Done(10): GetVisibleDtObjectAction");
        parser.parseLine("Done(10): GetDtObjectsAction");
        parser.parseLine("Done(10): GetDtObjectTreeSelectionStateAction");
        parser.parseLine("Done(10): AbstractGetDtObjectTemplateAction");
        parser.parseLine("Done(10): GetDtObjectTemplateAction");

        //then:
        Assert.assertEquals(5, parser.getDtObjectActions());
    }

}
