import React from 'react';
import './App.css';

class App extends React.Component {
    private myRef = React.createRef<HTMLHeadingElement>();

    constructor(props: any) {
        super(props);
        this.handler=this.handler.bind(this);
    }

    handler(e:any){
        // solution
        e.preventDefault();

        const url = 'https://jsonplaceholder.typicode.com/todos/1';
        fetch(url)
            .then(response => response.json())
            .then(json => {
                console.log(JSON.stringify(json));
                this.myRef.current!.innerText=(JSON.stringify(json));
            })
            .catch(error => console.error(error))
    }

    render(){
        return (
        <div>
            <h1 ref={this.myRef}>Print Json</h1>

            {/*success*/}
            <button onClick={this.handler}>button onClick</button>
            {/*fail*/}
            <form onSubmit={this.handler}>
                <input type={'submit'} value={'form submit'}/>
            </form>
            <button onClick={()=>window.location.reload()}> Refresh </button>
         </div>
    );
  }
}

export default App;