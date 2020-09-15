import React from 'react';
import './App.css';

class App extends React.Component {
    private myRef = React.createRef<HTMLHeadingElement>();

    constructor(props: any) {
        super(props);
        this.getHandler = this.getHandler.bind(this);
        this.postHandler_BodyToken = this.postHandler_BodyToken.bind(this);
        this.postHandler_headerToken = this.postHandler_headerToken.bind(this);
        this.getHandler_ParameterToken = this.getHandler_ParameterToken.bind(this);
    }

    getHandler(e: any) {
        // solution
        e.preventDefault();

        const url = 'https://jsonplaceholder.typicode.com/todos/1';
        fetch(url)
            .then(response => response.json())
            .then(json => {
                console.log(JSON.stringify(json));
                this.myRef.current!.innerText = (JSON.stringify(json));
            })
            .catch(error => console.error(error))
    }

    //body token
    postHandler_BodyToken(e: any) {
        e.preventDefault();

        const url = 'http://localhost:8000/auth/login'
        const content = {
            email: "nilson@email.com",
            password: "nilson"
        }
        const request: RequestInit = {
            method: 'POST',
            headers: {'Content-Type': 'Application/JSON'},
            body: JSON.stringify(content)
        }

        fetch(url, request)
            .then(response => response.json())
            .then(json => json['access_token'])
            .then(key => this.myRef.current!.innerText = key)
            .catch(error => console.error(error));
    }

    //parameter token
    getHandler_ParameterToken(e: any) {
        e.preventDefault();

        const url = 'http://localhost:8000/auth/login?'
            + 'email=nilson@email.com' + '&password=nilson'

        const request: RequestInit = {
            method: 'GET',
            headers: {'Content-Type': 'Application/JSON'},
        }

        fetch(url, request)
            .then(response => response.headers)
            .then(headers => headers.forEach(header => console.log(header)))
            .catch(error => console.error(error));
    }

    //header token
    postHandler_headerToken(e: any) {
        e.preventDefault();

        const url = 'http://localhost:8000/auth/login'
        const content = {
            email: "nilson@email.com",
            password: "nilson"
        }
        const request: RequestInit = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Expose-Headers': 'Authorization',
                'Access-Control-Allow-Methods': '*', // without 'Cookie' & 'Credentials'
                'Access-Control-Allow-Credentials': 'true' // allow 'Credentials'
            },
            body: JSON.stringify(content)
        }

        fetch(url, request)
            .then(response => response.headers)
            .then(headers => {
                Array.from(headers.keys()).forEach(header => console.log(header));
                console.log(headers.get('Authorization'));
            })
            .catch(error => console.error(error));
    }

    render() {
        return (
            <div>
                <h1 ref={this.myRef}>Print Json</h1>

                {/*success*/}
                <button onClick={this.postHandler_headerToken}>button onClick</button>
                {/*fail*/}
                <form onSubmit={this.postHandler_headerToken}>
                    <input type={'submit'} value={'form submit'}/>
                </form>
                <button onClick={() => window.location.reload()}> Refresh</button>
            </div>
        );
    }
}

export default App;