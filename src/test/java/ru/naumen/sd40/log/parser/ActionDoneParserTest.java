package ru.naumen.sd40.log.parser;

import org.junit.Assert;
import org.junit.Test;
import ru.naumen.sd40.log.parser.modes.sdng.data.SdngDataSet;
import ru.naumen.sd40.log.parser.modes.sdng.parser.SdngDataParser;

public class ActionDoneParserTest {
    private SdngDataSet ds = new SdngDataSet();

    @Test
    public void mustParseAddAction() {
        //given
        SdngDataParser parser = new SdngDataParser();

        //when
        parser.parseActionLine("Done(10): AddObjectAction", ds);

        //then
        Assert.assertEquals(1, ds.getActionDataSet().getAddObjectActions());
    }

    @Test
    public void mustParseFormActions() {
        //given
        SdngDataParser parser = new SdngDataParser();

        //when
        parser.parseActionLine("Done(10): GetFormAction", ds);
        parser.parseActionLine("Done(1): GetAddFormContextDataAction", ds);

        //then
        Assert.assertEquals(2, ds.getActionDataSet().getFormActions());
    }

    @Test
    public void mustParseEditObject() {
        //given
        SdngDataParser parser =  new SdngDataParser();

        //when
        parser.parseActionLine("Done(10): EditObjectAction", ds);

        //then
        Assert.assertEquals(1, ds.getActionDataSet().getEditObjectsActions());
    }

    @Test
    public void mustParseSearchObject(){
        //given
        SdngDataParser parser =  new SdngDataParser();

        //when
        parser.parseActionLine("Done(10): GetPossibleAgreementsChildsSearchAction", ds);
        parser.parseActionLine("Done(10): TreeSearchAction", ds);
        parser.parseActionLine("Done(10): GetSearchResultAction", ds);
        parser.parseActionLine("Done(10): GetSimpleSearchResultsAction", ds);
        parser.parseActionLine("Done(10): SimpleSearchAction", ds);
        parser.parseActionLine("Done(10): ExtendedSearchByStringAction", ds);
        parser.parseActionLine("Done(10): ExtendedSearchByFilterAction", ds);

        //then
        Assert.assertEquals(7, ds.getActionDataSet().getSearchActions());
    }

    @Test
    public void mustParseGetList(){
        //given:
        SdngDataParser parser =  new SdngDataParser();

        //when:
        parser.parseActionLine("Done(10): GetDtObjectListAction", ds);
        parser.parseActionLine("Done(10): GetPossibleCaseListValueAction", ds);
        parser.parseActionLine("Done(10): GetPossibleAgreementsTreeListActions", ds);
        parser.parseActionLine("Done(10): GetCountForObjectListAction", ds);
        parser.parseActionLine("Done(10): GetDataForObjectListAction", ds);
        parser.parseActionLine("Done(10): GetPossibleAgreementsListActions", ds);
        parser.parseActionLine("Done(10): GetDtObjectForRelObjListAction", ds);

        //then:
        Assert.assertEquals(7, ds.getActionDataSet().geListActions());
    }

    @Test
    public void mustParseComment(){
        //given:
        SdngDataParser parser =  new SdngDataParser();

        //when:
        parser.parseActionLine("Done(10): EditCommentAction", ds);
        parser.parseActionLine("Done(10): ChangeResponsibleWithAddCommentAction", ds);
        parser.parseActionLine("Done(10): ShowMoreCommentAttrsAction", ds);
        parser.parseActionLine("Done(10): CheckObjectsExceedsCommentsAmountAction", ds);
        parser.parseActionLine("Done(10): GetAddCommentPermissionAction", ds);
        parser.parseActionLine("Done(10): GetCommentDtObjectTemplateAction", ds);

        //then:
        Assert.assertEquals(6, ds.getActionDataSet().getCommentActions());
    }

    @Test
    public void mustParseDtObject(){
        //given:
        SdngDataParser parser =  new SdngDataParser();


        //when:
        parser.parseActionLine("Done(10): GetVisibleDtObjectAction", ds);
        parser.parseActionLine("Done(10): GetDtObjectsAction", ds);
        parser.parseActionLine("Done(10): GetDtObjectTreeSelectionStateAction", ds);
        parser.parseActionLine("Done(10): AbstractGetDtObjectTemplateAction", ds);
        parser.parseActionLine("Done(10): GetDtObjectTemplateAction", ds);

        //then:
        Assert.assertEquals(5, ds.getActionDataSet().getDtObjectActions());
    }

}
