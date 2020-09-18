import React from 'react';
import {BrowserRouter as Router, Link} from 'react-router-dom'
import MyRouter from "./MyRouter";

class Navi extends React.Component {
    render() {
        return (
            <Router>
                <nav className="crumbs">
                    <ul>
                        <li><Link to={'/'}>Home</Link></li>
                        <li><Link to={'/board'}>Board</Link></li>
                        <li><Link to={'/board/1'}>Test1</Link></li>
                        <li><Link to={'/board/2'}>Test2</Link></li>
                        <li><Link to={'/board/3'}>Test3</Link></li>
                    </ul>
                </nav>

                <MyRouter></MyRouter>
            </Router>
        );
    }
}

export default Navi;