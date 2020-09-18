import React from 'react';
import CKEditor from '@ckeditor/ckeditor5-react';
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
// import CKFinder from '@ckeditor/ckeditor5-ckfinder/src/ckfinder';
// https://ckeditor.com/docs/ckeditor5/latest/builds/guides/integration/installing-plugins.html#adding-a-plugin-to-a-build

// type Props = {}
// type State = { content:string }
export default class extends React.Component {
    editor = React.createRef();
    handleClick = () => console.log(this.state.content);
    handleBlur = (event, editor) => {
        console.log("Blur");
        this.setState({content: editor.getData()});
    }

    componentDidMount() {console.log('Mount')}
    componentDidUpdate(prevProps, prevState, snapshot) {console.log('Update')}
    componentDidCatch(error, errorInfo) {console.log('Catch')}
    componentWillUnmount() {console.log('Unmount')}
    shouldComponentUpdate(nextProps, nextState, nextContext) {return false}

    constructor(props) {
        super(props);
        this.state = {
            content: ''
        }
    }

    render() {
        const Example = () => {
            return (
                <CKEditor
                    ref={this.editor}
                    editor={ClassicEditor}
                    data={this.state.content}
                    // plugin={[CKFinder]}
                    onBlur={ ( event, editor ) => this.handleBlur(event, editor)}
                />
            )
        }

        return (
            <>
                <Example></Example>
                <button onClick={this.handleClick}>submit</button>
            </>
        );
    }

}