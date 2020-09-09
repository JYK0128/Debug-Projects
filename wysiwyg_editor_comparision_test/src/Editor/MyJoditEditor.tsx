import React from 'react';
import JoditEditor from "jodit-react";

type Props = {}
type State = { content:string }
export default class extends React.Component<Props, State>{
    private editor = React.createRef<any>();
    private handleClick = () => console.log(this.state.content);
    private handleBlur = () => {
        console.log("Blur");
        this.setState({content: this.editor.current!.value})
    }

    componentDidMount() {console.log('Mount')}
    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any) {console.log('Update')}
    componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {console.log('Catch')}
    componentWillUnmount() {console.log('Unmount')}
    shouldComponentUpdate(nextProps: Readonly<Props>, nextState: Readonly<State>, nextContext: any): boolean {return false}

    constructor(props:Props) {
        super(props);
        this.state={
            content:''
        }
    }

    render(){
        const config = {
            readonly: false,
        }

        const Example = () => {
            return (
                <JoditEditor
                    ref={this.editor}
                    value={this.state.content}
                    config={config as any}
                    onBlur={() => this.handleBlur()}
                />
            );
        }

        return(
            <>
                <Example></Example>
                <button onClick={this.handleClick}>submit</button>
            </>
        );
    }
}