import React from 'react'
// Editor
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import {Editor} from '@toast-ui/react-editor';
// color picker
import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';
// highlight
import 'highlight.js/styles/github.css';
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight';
// merged cell
import tableMergedCell from '@toast-ui/editor-plugin-table-merged-cell';
//chart
import 'tui-chart/dist/tui-chart.css';
import chart from '@toast-ui/editor-plugin-chart';
//uml
import uml from '@toast-ui/editor-plugin-uml';

type Props = {}
type State = { content: string }
export default class extends React.Component<Props, State> {
    private editor = React.createRef<any>();
    private handleClick = () => console.log(this.state.content);
    private handleBlur = () => {
        console.log("Blur");
        this.setState({content: this.editor.current!.getInstance().getMarkdown()}); //getHtml()
    }

    componentDidMount() {
        console.log('Mount')
    }

    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any) {
        console.log('Update')
    }

    componentDidCatch(error: Error, errorInfo: React.ErrorInfo) {
        console.log('Catch')
    }

    componentWillUnmount() {
        console.log('Unmount')
    }

    shouldComponentUpdate(nextProps: Readonly<Props>, nextState: Readonly<State>, nextContext: any): boolean {
        return false
    }


    constructor(props: Props) {
        super(props);
        this.state = {
            content: ''
        }
    }

    uploadImage(blob: any) {
        //before upload file server

        //after upload file server
        return 'https://img-s-msn-com.akamaized.net/tenant/amp/entityid/BB18LSrq.img';
    }

    render() {
        const Example = () => {
            return (
                <Editor
                    initialValue={this.state.content}
                    previewStyle="vertical"
                    initialEditType="markdown"
                    useCommandShortcut={true}
                    plugins={[tableMergedCell as any, colorSyntax, codeSyntaxHighlight, chart, uml as any]}
                    usageStatistics={false}
                    events={{blur: this.handleBlur}}
                    ref={this.editor}
                    hooks={{
                        addImageBlobHook: (blob, callback) => {
                            const uploadedImageURL = this.uploadImage(blob);
                            callback(uploadedImageURL, "No image");
                        }
                    }}
                />
            )
        };

        return (
            <>
                <Example></Example>
                <button onClick={this.handleClick}>submit</button>
            </>
        );
    }
}