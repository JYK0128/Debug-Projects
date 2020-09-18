import React from 'react';
import {Route, Switch} from 'react-router-dom'
import MyCKEditor from "../Editor/MyCKEditor";
import MyToastEditor from "../Editor/MyToastEditor";
import MyJoditEditor from "../Editor/MyJoditEditor";

export default class extends React.Component {
    render() {
        return (
            <Switch>
                <Route exact path="/ck-editor" component={MyCKEditor}/>
                <Route exact path="/toast-editor" component={MyToastEditor}/>
                <Route exact path="/jodit-editor" component={MyJoditEditor}/>
            </Switch>
        );
    }
}