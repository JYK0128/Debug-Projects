import React from 'react';
import {Route, Switch} from 'react-router-dom'
import Home from "./Home";
import Board from "./Board";
import Detail from "./Detail"

class MyRouter extends React.Component {
    render() {
        return (
            <Switch>
                <Route exact path="/" component={Home}/>
                <Route exact path="/board" component={Board}/>
                <Route exact path="/board/:id" component={Detail}/>
            </Switch>
        );
    }
}

export default MyRouter;