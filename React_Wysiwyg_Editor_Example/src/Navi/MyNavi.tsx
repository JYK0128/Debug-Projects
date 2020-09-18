import React from 'react';
import {BrowserRouter as Router, Link} from 'react-router-dom'
import MyRouter from "./MyRouter";

export default class extends React.Component {
    render() {
        return (
            <Router>
                <nav>
                    <ul>
                        <li style={{display: 'inline', margin: '10px'}}><Link to={'/ck-Editor'}>CK Editor</Link></li>
                        <li style={{display: 'inline', margin: '10px'}}><Link to={'/toast-Editor'}>Toast Editor</Link></li>
                        <li style={{display: 'inline', margin: '10px'}}><Link to={'/Jodit-Editor'}>Jodit Editor</Link></li>
                    </ul>
                </nav>

                <MyRouter></MyRouter>
            </Router>
        );
    }
}